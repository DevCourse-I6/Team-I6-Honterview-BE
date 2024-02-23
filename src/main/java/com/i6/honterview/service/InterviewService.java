package com.i6.honterview.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Category;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.QuestionCategory;
import com.i6.honterview.domain.enums.InterviewStatus;
import com.i6.honterview.dto.request.InterviewCompleteRequest;
import com.i6.honterview.dto.request.InterviewCreateRequest;
import com.i6.honterview.dto.response.InterviewCompleteResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.AnswerRepository;
import com.i6.honterview.repository.InterviewRepository;
import com.i6.honterview.repository.MemberRepository;
import com.i6.honterview.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

	private final InterviewRepository interviewRepository;
	private final MemberRepository memberRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;

	public Long createInterview(InterviewCreateRequest request, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Question question = questionRepository.findById(request.questionId())
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		Interview interview = interviewRepository.save(request.toEntity(member, question));
		return interview.getId();
	}

	public void updateInterviewStatus(Long id) {
		Interview interview = interviewRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
		if (interview.getStatus().equals(InterviewStatus.IN_PROGRESS)) {
			interview.changeStatus(InterviewStatus.RESULT_CHECK);
		}
	}

	public void deleteInterview(Long id, Long memberId) {
		Interview interview = interviewRepository.findWithQuestionsById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		if (!interview.isSameInterviewee(memberId)) {
			throw new CustomException(ErrorCode.INTERVIEWEE_NOT_SAME);
		}

		if (!interview.isDeletable()) {
			throw new CustomException(ErrorCode.INTERVIEW_DELETE_FORBIDDEN);
		}
		interviewRepository.delete(interview);
	}

	public InterviewCompleteResponse completeInterviewAndSaveAnswers(Long id, Long memberId,
		InterviewCompleteRequest request) {
		Interview interview = interviewRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		// 면접 상태 변경 (RESULT_CHECK -> COMPLETED)
		interview.completeInterview();

		// 질문 & 답변 목록 저장
		List<InterviewCompleteRequest.QuestionAnswer> savedAnswers = new ArrayList<>();
		List<Category> categories = new ArrayList<>();
		Long parentQuestionId = 0L;

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		for (InterviewCompleteRequest.QuestionAnswer qa : request.questionAnswers()) {
			// 첫번째 질문은 답변만 저장
			if (qa.questionId() != null && qa.questionId() != -1) {
				Question question = questionRepository.findById(qa.questionId())
					.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
				// 첫번째 질문의 카테고리
				categories.addAll(question.getQuestionCategories().stream()
					.map(QuestionCategory::getCategory)
					.toList());
				parentQuestionId = question.getId();
				Answer answer = new Answer(qa.answerContent(), question, member, qa.visibility(), interview);
				answerRepository.save(answer);
				savedAnswers.add(qa);
			} else {
				// 새로운 질문과 답변 생성
				Question newQuestion = new Question(qa.questionContent(), parentQuestionId, categories, "ChatGPT");
				questionRepository.save(newQuestion);
				Answer newAnswer = new Answer(qa.answerContent(), newQuestion, member, qa.visibility(), interview);
				answerRepository.save(newAnswer);
				savedAnswers.add(new InterviewCompleteRequest.QuestionAnswer(qa.visibility(), newQuestion.getId(),
					qa.questionContent(), qa.answerContent()));
			}
		}

		return InterviewCompleteResponse.of(interview, savedAnswers);
	}
}
