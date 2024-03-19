package com.i6.honterview.domain.gpt.service;

import com.i6.honterview.domain.gpt.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.domain.gpt.dto.response.GptApiResponse;

public interface GptService {

	GptApiResponse createGptQuestion(String prompt);

	String generateTailQuestionPrompt(GptQuestionCreateRequest request);

	String generateNewQuestionPrompt(String oldQuestion);
}
