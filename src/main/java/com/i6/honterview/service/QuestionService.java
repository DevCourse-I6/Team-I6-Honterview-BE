package com.i6.honterview.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Category;
import com.i6.honterview.domain.Question;
import com.i6.honterview.dto.request.QuestionCreateRequest;
import com.i6.honterview.dto.request.QuestionUpdateRequest;
import com.i6.honterview.dto.response.AnswerResponse;
import com.i6.honterview.dto.response.PageResponse;
import com.i6.honterview.dto.response.QuestionDetailResponse;
import com.i6.honterview.dto.response.QuestionResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.AnswerRepository;
import com.i6.honterview.repository.CategoryRepository;
import com.i6.honterview.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {// TODO: 멤버&관리자 연동

	private static final int CATEGORY_MAX_COUNT = 3;

	private final QuestionRepository questionRepository;
	private final CategoryRepository categoryRepository;
	private final AnswerRepository answerRepository;

	@Transactional(readOnly = true)
	public PageResponse<QuestionResponse> getQuestions(int page, int size, String query, List<String> categoryNames,
		String orderType) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Question> questions = questionRepository.
			findQuestionsByKeywordAndCategoryNamesWithPage(pageable, query, categoryNames, orderType);
		return PageResponse.of(questions, QuestionResponse::from);
	}

	@Transactional(readOnly = true)
	public QuestionDetailResponse getQuestionById(Long id, int page, int size) {
		Question question = questionRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Answer> answers = answerRepository.findByQuestionIdWithMember(id, pageable);
		PageResponse<AnswerResponse> answerResponse = PageResponse.of(answers, AnswerResponse::from);

		return QuestionDetailResponse.from(question, answerResponse);
	}

	public List<QuestionResponse> getRandomQuestionsByCategories(Long questionId) {
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		List<Long> categoryIds = question.getQuestionCategories().stream()
			.map(qc -> qc.getCategory().getId())
			.toList();

		// 조회된 카테고리에 속하는 랜덤 질문 3개 조회 (현재 질문 제외)
		List<Question> randomQuestions = questionRepository.findRandomQuestionsByCategoryIds(categoryIds,
			question.getId());

		return randomQuestions.stream()
			.map(QuestionResponse::from)
			.toList();
	}

	public Long createQuestion(QuestionCreateRequest request) {
		validateCategoryIds(request.categoryIds());
		List<Category> categories = findAndValidateCategories(request.categoryIds());

		String creator = "MEMBER_1"; // TODO: role에 따른 질문 생성자 정보 저장
		Question question = questionRepository.save(request.toEntity(categories, creator));
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
