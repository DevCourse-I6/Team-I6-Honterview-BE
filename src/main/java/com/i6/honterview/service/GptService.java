package com.i6.honterview.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.i6.honterview.config.GptConfig;
import com.i6.honterview.domain.enums.InterviewStatus;
import com.i6.honterview.dto.request.GptApiRequest;
import com.i6.honterview.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.dto.request.Message;
import com.i6.honterview.dto.response.GptApiResponse;
import com.i6.honterview.dto.response.GptQuestionCreateResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.exception.OpenAiException;
import com.i6.honterview.repository.InterviewRepository;
import com.i6.honterview.response.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {
	@Value("${openai.model}")
	private String model;

	@Value("${openai.end-point}")
	private String endPoint;
	private final GptConfig gptConfig;
	private final ObjectMapper objectMapper;
	private final InterviewRepository interviewRepository; //TODO: service로 변경

	// TODO: 호출 횟수 제한
	public GptQuestionCreateResponse createGptQuestion(Long interviewId, GptQuestionCreateRequest request) {
		validateInterviewStatus(interviewId);

		String prompt = generatePrompt(request);
		GptApiRequest apiRequest = new GptApiRequest(model, List.of(new Message("user", prompt)));
		try {
			GptApiResponse response = invokeGptApi(apiRequest);
			validateApiResponse(response);
			return GptQuestionCreateResponse.from(response.id(), response.choices().get(0));
		} catch (HttpClientErrorException e) {
			ErrorResponse errorResponse = handleHttpClientErrorException(e.getResponseBodyAsString());
			HttpStatus status = (HttpStatus)e.getStatusCode();
			throw new OpenAiException(errorResponse, status);
		} catch (Exception e) {
			log.error("Exception Occurred during creating tail question. : {}", e);
			throw new CustomException(ErrorCode.GPT_QUESTION_CREATE_FAIL);
		}
	}

	private String generatePrompt(GptQuestionCreateRequest request) {
		return """
            면접 질문 : %s
            답변 : %s
            이에 대한 꼬리 질문 하나만 생성
            """.formatted(request.prevQuestion(), request.prevAnswer());
	}

	private GptApiResponse invokeGptApi(GptApiRequest apiRequest) {
		return gptConfig.restTemplate().postForObject(endPoint, apiRequest, GptApiResponse.class);
	}

	private void validateApiResponse(GptApiResponse response) {
		String finish_reason = response.choices().get(0).finish_reason();
		if (!finish_reason.equals("stop")) {
			log.warn("Creating question failed. finish_reason : {}", finish_reason);
			throw new CustomException(ErrorCode.GPT_QUESTION_CREATE_FAIL);
		}
	}

	private void validateInterviewStatus(Long interviewId) {
		boolean isInterviewing = interviewRepository.existsByIdAndStatus(interviewId, InterviewStatus.IN_PROGRESS);
		if (!isInterviewing) {
			throw new CustomException(ErrorCode.INTERVIEW_NOT_PROCESSING);
		}
	}

	private ErrorResponse handleHttpClientErrorException(String body) {
		if (body == null || body.isBlank()) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		try {
			JsonNode jsonNode = objectMapper.readTree(body);
			String code = jsonNode.path("error").path("type").asText();
			String message = jsonNode.path("error").path("message").asText();
			return new ErrorResponse(code, message);
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
