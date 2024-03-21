package com.i6.honterview.domain.gpt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.i6.honterview.common.dto.ErrorResponse;
import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.common.exception.OpenAiException;
import com.i6.honterview.domain.gpt.dto.request.GptApiRequest;
import com.i6.honterview.domain.gpt.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.domain.gpt.dto.request.Message;
import com.i6.honterview.domain.gpt.dto.response.GptApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptServiceImpl implements GptService {
	@Value("${openai.model}")
	private String model;
	private final ObjectMapper objectMapper;
	private final GptFeignClient gptFeignClient;

	@Override
	public GptApiResponse createGptQuestion(String prompt) {
		GptApiRequest apiRequest = new GptApiRequest(model, List.of(new Message("user", prompt)));
		try {
			GptApiResponse response =  gptFeignClient.createTailQuestion(apiRequest);
			validateApiResponse(response);
			return response;
		} catch (HttpClientErrorException e) {
			ErrorResponse errorResponse = handleHttpClientErrorException(e.getResponseBodyAsString());
			HttpStatus status = (HttpStatus)e.getStatusCode();
			throw new OpenAiException(errorResponse, status);
		} catch (Exception e) {
			log.error("Exception Occurred during creating GPT question: {}", e);
			throw new CustomException(ErrorCode.GPT_QUESTION_CREATE_FAIL);
		}
	}

	@Override
	public String generateTailQuestionPrompt(GptQuestionCreateRequest request) {
		return """
			면접 질문 : %s
			답변 : %s
			이에 대한 꼬리 질문 하나만 생성
			""".formatted(request.prevQuestion(), request.prevAnswer());
	}

	@Override
	public String generateNewQuestionPrompt(String oldQuestion) {
		return """
			"%s"
			위 면접 질문과 유사한 직무의 다른 주제 면접 질문 하나만 생성. no yap. no double quotes
			""".formatted(oldQuestion);
	}

	private void validateApiResponse(GptApiResponse response) {
		String finish_reason = response.choices().get(0).finish_reason();
		if (!finish_reason.equals("stop")) {
			log.warn("Creating question failed. finish_reason : {}", finish_reason);
			throw new CustomException(ErrorCode.GPT_QUESTION_CREATE_FAIL);
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
