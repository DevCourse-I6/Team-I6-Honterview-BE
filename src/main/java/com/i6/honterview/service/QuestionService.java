package com.i6.honterview.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Category;
import com.i6.honterview.domain.Question;
import com.i6.honterview.dto.request.QuestionCreateRequest;
import com.i6.honterview.dto.request.QuestionUpdateRequest;
import com.i6.honterview.dto.response.PageResponse;
import com.i6.honterview.dto.response.QuestionDetailResponse;
import com.i6.honterview.dto.response.QuestionResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.CategoryRepository;
import com.i6.honterview.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

	private static final int CATEGORY_MAX_COUNT = 3;

	private final QuestionRepository questionRepository;
	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public PageResponse<QuestionResponse> getQuestions(int page, int size, String query, List<String> categoryNames,
		String orderType) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Question> questions = questionRepository.
			findQuestionsByKeywordAndCategoryNamesWithPage(pageable, query, categoryNames, orderType);
		return PageResponse.of(questions, QuestionResponse::from);
	}

	@Transactional(readOnly = true)
	public QuestionDetailResponse getQuestionById(Long id) {
		// TODO : 답변 목록 추가
		Question question = questionRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
		return QuestionDetailResponse.from(question);
	}

	public Long createQuestion(QuestionCreateRequest request) {
		validateCategoryIds(request.categoryIds());

		List<Category> categories = findAndValidateCategories(request.categoryIds());
		Question question = questionRepository.save(request.toEntity(categories));
		return question.getId();
	}

	public void updateQuestion(Long id, QuestionUpdateRequest request) {
		Question question = questionRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		List<Category> categories = findAndValidateCategories(request.categoryIds());
		question.changeContentAndCategories(request.content(), categories);
	}

	private void validateCategoryIds(List<Long> categoryIds) {
		int categoryIdsSize = categoryIds.size();
		if (categoryIdsSize == 0 || categoryIdsSize > CATEGORY_MAX_COUNT)
			throw new CustomException(ErrorCode.INVALID_CATEGORY_COUNT);
	}

	private List<Category> findAndValidateCategories(List<Long> categoryIds) {
		List<Category> categories = categoryRepository.findAllById(categoryIds);
		// Question은 적어도 하나 이상의 카테고리와 연결되어 있어야 함
		if (categories.isEmpty())
			throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
		return categories;
	}

	public void deleteQuestion(Long id) {
		Question question = questionRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
		questionRepository.delete(question);
		// TODO : 연관된 답변 삭제
	}
}
