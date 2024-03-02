package com.i6.honterview.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.i6.honterview.config.GptConfig;
import com.i6.honterview.dto.request.GptApiRequest;
import com.i6.honterview.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.dto.request.Message;
import com.i6.honterview.dto.response.GptQuestionCreateResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;

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
		String prompt = "면접 질문 : " + request.prevQuestion() + "답변 : " + request.prevAnswer()
			+ "이에 대한 꼬리 질문 하나만 생성";
		GptApiRequest apiRequest = new GptApiRequest(
			model,
			List.of(new Message("user", prompt))
		);

		GptQuestionCreateResponse response  = gptConfig.restTemplate().postForObject(endPoint, apiRequest, GptQuestionCreateResponse.class);
		if (response != null) {
			return response;
		} else {

			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		}
	}
}
