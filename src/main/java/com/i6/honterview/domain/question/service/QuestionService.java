package com.i6.honterview.domain.question.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.common.dto.PageResponse;
import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.answer.dto.response.AnswerResponse;
import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.answer.service.AnswerService;
import com.i6.honterview.domain.question.dto.request.QuestionCreateRequest;
import com.i6.honterview.domain.question.dto.request.QuestionUpdateRequest;
import com.i6.honterview.domain.question.dto.request.TailQuestionSaveRequest;
import com.i6.honterview.domain.question.dto.response.QuestionDetailResponse;
import com.i6.honterview.domain.question.dto.response.QuestionResponse;
import com.i6.honterview.domain.question.dto.response.QuestionWithCategoriesResponse;
import com.i6.honterview.domain.question.entity.Category;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {// TODO: 멤버&관리자 연동

	private final QuestionRepository questionRepository;
	private final AnswerService answerService;
	private final CategoryService categoryService;

	@Transactional(readOnly = true)
	public PageResponse<QuestionWithCategoriesResponse> getQuestions(int page, int size, String query,
		List<String> categoryNames,
		String orderType) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Question> questions = questionRepository.
			findQuestionsByKeywordAndCategoryNamesWithPage(pageable, query, categoryNames, orderType);
		return PageResponse.of(questions, QuestionWithCategoriesResponse::from);
	}

	@Transactional(readOnly = true)
	public QuestionDetailResponse getQuestionById(Long id, int page, int size) {
		Question question = questionRepository.findQuestionByIdWithCategories(id)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Answer> answers = answerService.findByQuestionIdWithMember(id, pageable);
		PageResponse<AnswerResponse> answerResponse = PageResponse.of(answers, AnswerResponse::from);

		return QuestionDetailResponse.from(question, answerResponse);
	}

	public List<QuestionResponse> getRandomTailQuestions(Long parentId) {
		List<Question> tailQuestions = questionRepository.findRandomTailQuestionsByParentId(parentId);
		return tailQuestions.stream()
			.map(QuestionResponse::from)
			.toList();
	}

	public QuestionResponse createQuestion(QuestionCreateRequest request, String creator) {
		List<Category> categories = categoryService.validateAndGetCategories(request.categoryIds());
		Question question = questionRepository.save(request.toEntity(categories, creator));
		return QuestionResponse.from(question);
	}

	public Question saveTailQuestion(TailQuestionSaveRequest request) {
		List<Category> categories = categoryService.validateAndGetCategories(request.categoryIds());
		return questionRepository.save(request.toEntity(categories));
	}

	public void updateQuestion(Long id, QuestionUpdateRequest request) {
		Question question = findById(id);

		List<Category> categories = categoryService.validateAndGetCategories(request.categoryIds());
		question.changeContentAndCategories(request.content(), categories);
	}

	public void deleteQuestion(Long id) {
		Question question = findById(id);
		questionRepository.delete(question);
	}

	public List<QuestionResponse> getQuestionsByCategoryNames(List<String> categoryNames) {
		List<Question> questions = questionRepository.findQuestionsByCategoryNames(categoryNames);
		return questions.stream()
			.map(QuestionResponse::from)
			.toList();
	}

	public PageResponse<QuestionWithCategoriesResponse> getBookmarkedQuestionsMypage(int page, int size,
		Long memberId) {
		Pageable pageable = PageRequest.of(page - 1, size);  // TODO : PageRequest 리팩토링
		Page<Question> questions = questionRepository.findByMemberIdWithPage(pageable, memberId);
		return PageResponse.of(questions, QuestionWithCategoriesResponse::from);
	}

	public Question findByIdWithHearts(Long questionId) {
		return questionRepository.findByIdWithHearts(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
	}

	public Question findById(Long questionId) {
		return questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
	}
}
