package com.i6.honterview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Question;
import com.i6.honterview.dto.PageResponse;
import com.i6.honterview.dto.QuestionDetailResponse;
import com.i6.honterview.dto.QuestionResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.QuestionQueryDslRepository;
import com.i6.honterview.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

	private final QuestionQueryDslRepository questionQueryDslRepository;
	private final QuestionRepository questionRepository;

	@Transactional(readOnly = true)
	public PageResponse<QuestionResponse> getQuestions(int page, int size, String query) {
		// TODO : 좋아요 순 정렬, 카테고리 목록별 검색 추가
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Question> questions = questionQueryDslRepository.findQuestionsByKeywordWithPage(pageable, query);
		return PageResponse.of(questions, QuestionResponse::new);
	}

	@Transactional(readOnly = true)
	public QuestionDetailResponse getQuestionById(Long id) {
		// TODO : 답변 목록 추가
		Question question = questionRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
		return QuestionDetailResponse.from(question);
	}

}
