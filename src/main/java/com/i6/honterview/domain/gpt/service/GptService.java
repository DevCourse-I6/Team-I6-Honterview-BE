package com.i6.honterview.domain.gpt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.i6.honterview.domain.gpt.dto.request.GptNewQuestionCreateRequest;
import com.i6.honterview.domain.gpt.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.domain.gpt.dto.request.Message;
import com.i6.honterview.domain.gpt.dto.response.GptApiResponse;
import com.i6.honterview.domain.gpt.dto.response.GptQuestionCreateResponse;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.interview.service.InterviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {
	@Value("${openai.model}")
	private String model;
	private final ObjectMapper objectMapper;
	private final InterviewService interviewService;
	private final GptFeignClient gptFeignClient;
	private final RedisTemplate<Long, Long> redisCountTemplate;


	public GptQuestionCreateResponse createTailGptQuestion(Long interviewId, GptQuestionCreateRequest request) {
		Interview interview = interviewService.getProcessingInterview(interviewId);
		checkAndIncrementCallCount(interviewId, (long)interview.getQuestionCount());
		String prompt = generateTailQuestionPrompt(request);
		return createGptQuestion(prompt);
	}

	public GptQuestionCreateResponse createNewGptQuestion(Long interviewId, GptNewQuestionCreateRequest request) {
		Interview interview = interviewService.getProcessingInterview(interviewId);
		checkAndIncrementCallCount(interviewId, (long)interview.getQuestionCount());
		String prompt = generateNewQuestionPrompt(request.prevQuestion());
		return createGptQuestion(prompt);
	}

	private GptQuestionCreateResponse createGptQuestion(String prompt) {
		GptApiRequest apiRequest = new GptApiRequest(model, List.of(new Message("user", prompt)));
		try {
			GptApiResponse response = gptFeignClient.createTailQuestion(apiRequest);
			validateApiResponse(response);
			return GptQuestionCreateResponse.from(response.id(), response.choices().get(0));
		} catch (HttpClientErrorException e) {
			ErrorResponse errorResponse = handleHttpClientErrorException(e.getResponseBodyAsString());
			HttpStatus status = (HttpStatus)e.getStatusCode();
			throw new OpenAiException(errorResponse, status);
		} catch (Exception e) {
			log.error("Exception Occurred during creating GPT question: {}", e);
			throw new CustomException(ErrorCode.GPT_QUESTION_CREATE_FAIL);
		}
	}

	private void checkAndIncrementCallCount(Long interviewId, Long questionCount) {// TODO: redis에 limit이 없을 경우의 예외 처리
		Long limit = questionCount * 2;
		Long count = redisCountTemplate.opsForValue().increment(interviewId, 1);
		if (count > limit) {
			throw new CustomException(ErrorCode.CALL_LIMIT_EXCEEDED);
		}
	}

	private String generateTailQuestionPrompt(GptQuestionCreateRequest request) {
		return """
			면접 질문 : %s
			답변 : %s
			이에 대한 꼬리 질문 하나만 생성
			""".formatted(request.prevQuestion(), request.prevAnswer());
	}

	private String generateNewQuestionPrompt(String oldQuestion) {
		return """
			면접 질문 : %s,
			위 면접 질문과 유사한 직무의 다른 주제 면접 질문 하나만 생성. 질문만 출력
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
