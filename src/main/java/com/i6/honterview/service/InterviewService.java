package com.i6.honterview.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.InterviewQuestion;
import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.enums.InterviewStatus;
import com.i6.honterview.dto.request.AnswerCreateRequest;
import com.i6.honterview.dto.request.AnswerVisibilityUpdateRequest;
import com.i6.honterview.dto.request.InterviewCreateRequest;
import com.i6.honterview.dto.request.QuestionAnswerCreateRequest;
import com.i6.honterview.dto.request.TailQuestionSaveRequest;
import com.i6.honterview.dto.response.AnswersVisibilityUpdateResponse;
import com.i6.honterview.dto.response.InterviewInfoResponse;
import com.i6.honterview.dto.response.QuestionAnswerCreateResponse;
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
	private final QuestionService questionService;
	private final AnswerService answerService;
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
		Interview interview = interviewRepository.findByIdWithInterviewQuestions(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		if (!interview.isSameInterviewee(memberId)) {
			throw new CustomException(ErrorCode.INTERVIEWEE_NOT_SAME);
		}

		if (!interview.isDeletable()) {
			throw new CustomException(ErrorCode.INTERVIEW_DELETE_FORBIDDEN);
		}
		interviewRepository.delete(interview);
	}

	public QuestionAnswerCreateResponse createQuestionAndAnswer(Long id, QuestionAnswerCreateRequest request) {
		Interview interview = interviewRepository.findByIdWithInterviewQuestions(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		Question firstQuestion = getFirstQuestionFromInterview(interview);

		Question question;
		if (request.sequence() != 1) { // 첫번째 질문이 아닐 경우 질문 저장
			question = createQuestion(request.questionContent(), firstQuestion);
			interview.addQuestion(question, request.processingTime());
		} else {
			question = firstQuestion;
		}
		Answer answer = createAnswer(request.answerContent(), interview, question);
		return QuestionAnswerCreateResponse.of(question, answer);
	}

	public InterviewInfoResponse getInterviewInfo(Long id) {
		Interview interview = interviewRepository.findByIdWithInterviewQuestions(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		Question question = getFirstQuestionFromInterview(interview);
		return InterviewInfoResponse.of(interview, question.getQuestionCategories());
	}

	private Answer createAnswer(String answerContent, Interview interview, Question question) {
		AnswerCreateRequest answerCreateRequest = new AnswerCreateRequest(answerContent);
		return answerService.createAnswer(answerCreateRequest, question, interview);
	}

	private Question createQuestion(String questionContent, Question firstQuestion) {
		TailQuestionSaveRequest tailQuestionSaveRequest = new TailQuestionSaveRequest(
			questionContent, firstQuestion.getId(), firstQuestion.getCategoryIds());
		return questionService.saveTailQuestion(tailQuestionSaveRequest);
	}

	private Question getFirstQuestionFromInterview(Interview interview) {
		return interview.getInterviewQuestions().stream()
			.findFirst()
			.map(InterviewQuestion::getQuestion)
			.orElseThrow(() -> new CustomException(ErrorCode.FIRST_QUESTION_NOT_FOUND));
	}

	public AnswersVisibilityUpdateResponse changeAnswersVisibility(Long interviewId,
		List<AnswerVisibilityUpdateRequest> request) {
		Interview interview = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		List<Answer> answers = answerRepository.findByInterviewId(interview.getId());
		Map<Long, Answer> answerMap = answers.stream()
			.collect(Collectors.toMap(Answer::getId, Function.identity()));

		request.forEach(req -> {
			Answer answer = Optional.ofNullable(answerMap.get(req.answerId()))
				.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

			answer.changeVisibility(req.visibility());
		});

		return new AnswersVisibilityUpdateResponse(interview.getId());
	}
}
