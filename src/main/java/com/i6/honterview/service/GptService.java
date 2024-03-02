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
import com.i6.honterview.dto.request.GptApiRequest;
import com.i6.honterview.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.dto.request.Message;
import com.i6.honterview.dto.response.GptQuestionCreateResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.exception.OpenAiException;
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

	public GptQuestionCreateResponse createGptQuestion(GptQuestionCreateRequest request) {
		String prompt = "면접 질문 : " + request.prevQuestion()
			+ "답변 : " + request.prevAnswer()
			+ "이에 대한 꼬리 질문 하나만 생성";
		GptApiRequest apiRequest = new GptApiRequest(model, List.of(new Message("user", prompt)));
		try {
			return gptConfig.restTemplate().postForObject(endPoint, apiRequest, GptQuestionCreateResponse.class);
		} catch (HttpClientErrorException e) {
			ErrorResponse errorResponse = handleHttpClientErrorException(e.getResponseBodyAsString());
			HttpStatus status = (HttpStatus)e.getStatusCode();
			throw new OpenAiException(errorResponse, status);
		} catch (Exception e) {
			log.error("Exception Occurred during creating tail question. : {}", e);
			throw new CustomException(ErrorCode.GPT_QUESTION_CREATE_FAIL);
		}
	}

	private ErrorResponse handleHttpClientErrorException(String body) {
		if (body == null || body.isBlank()) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(body);
			String code = jsonNode.path("error").path("type").asText();
			String message = jsonNode.path("error").path("message").asText();
			return new ErrorResponse(code, message);
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
