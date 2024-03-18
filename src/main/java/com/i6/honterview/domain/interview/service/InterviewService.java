package com.i6.honterview.domain.interview.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.common.dto.PageRequest;
import com.i6.honterview.common.dto.PageResponse;
import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.answer.dto.request.AnswerCreateRequest;
import com.i6.honterview.domain.answer.dto.request.AnswerVisibilityUpdateRequest;
import com.i6.honterview.domain.answer.dto.response.AnswersVisibilityUpdateResponse;
import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.answer.service.AnswerService;
import com.i6.honterview.domain.interview.dto.request.InterviewCreateRequest;
import com.i6.honterview.domain.interview.dto.request.QuestionAnswerCreateRequest;
import com.i6.honterview.domain.interview.dto.response.InterviewInfoResponse;
import com.i6.honterview.domain.interview.dto.response.QuestionAndAnswerResponse;
import com.i6.honterview.domain.interview.dto.response.QuestionAnswerCreateResponse;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.interview.entity.InterviewStatus;
import com.i6.honterview.domain.interview.repository.InterviewRepository;
import com.i6.honterview.domain.question.dto.request.TailQuestionSaveRequest;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.question.service.QuestionService;
import com.i6.honterview.domain.user.dto.response.InterviewMypageResponse;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

	private final InterviewRepository interviewRepository;
	private final MemberService memberService;
	private final QuestionService questionService;
	private final AnswerService answerService;

	public Long createInterview(InterviewCreateRequest request, Long memberId) {
		Member member = memberService.findById(memberId);

		Question question = questionService.findById(request.questionId());

		Interview interview = interviewRepository.save(request.toEntity(member, question));
		return interview.getId();
	}

	public void updateInterviewStatus(Long interviewId, Long memberId) {
		Interview interview = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
		interview.validateInterviewee(memberId);

		if (interview.getStatus().equals(InterviewStatus.IN_PROGRESS)) {
			interview.changeStatus(InterviewStatus.COMPLETED);
		}
	}

	public void deleteInterview(Long id, Long memberId) {
		Interview interview = interviewRepository.findByIdWithInterviewQuestions(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
		interview.validateInterviewee(memberId);

		if (!interview.isDeletable()) {
			throw new CustomException(ErrorCode.INTERVIEW_DELETE_FORBIDDEN);
		}
		interviewRepository.delete(interview);
	}

	public QuestionAnswerCreateResponse createQuestionAndAnswer(Long id, Long memberId,
		QuestionAnswerCreateRequest request) {
		Interview interview = interviewRepository.findByIdWithInterviewQuestions(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
		interview.validateInterviewee(memberId);

		validateAnswerCount(interview);

		Question firstQuestion = interview.findFirstQuestion();
		Question question;
		if (interview.getAnswers().isEmpty()) { // 첫번째 질문 -> 답변만 생성
			question = firstQuestion;
		} else { // 이후 꼬리질문 -> 질문, 답변 생성
			question = createQuestion(request.questionContent(), firstQuestion);
			interview.addQuestion(question);
		}

		Answer answer = createAnswer(request.answerContent(), request.processingTime(), interview, question);
		return QuestionAnswerCreateResponse.of(question, answer);
	}

	public InterviewInfoResponse getInterviewInfo(Long interviewId, Long memberId) {
		Interview interview = interviewRepository.findByIdWithInterviewQuestions(interviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
		interview.validateInterviewee(memberId);

		List<QuestionAndAnswerResponse> questionsAndAnswers = createQuestionAndAnswerResponses(interview);
		return InterviewInfoResponse.of(interview, questionsAndAnswers);
	}

	public AnswersVisibilityUpdateResponse changeAnswersVisibility(Long interviewId,
		List<AnswerVisibilityUpdateRequest> request, Long memberId) {
		Interview interview = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
		interview.validateInterviewee(memberId);

		List<Answer> answers = answerService.findByInterviewId(interview.getId());
		Map<Long, Answer> answerMap = answers.stream()
			.collect(Collectors.toMap(Answer::getId, Function.identity()));

		request.forEach(req -> {
			Answer answer = Optional.ofNullable(answerMap.get(req.answerId()))
				.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

			answer.changeVisibility(req.visibility());
		});

		return new AnswersVisibilityUpdateResponse(interview.getId());
	}

	public PageResponse<InterviewMypageResponse> getInterviewsMypage(Long memberId, PageRequest pageRequest) {
		Pageable pageable = pageRequest.getPageable();
		Page<Interview> interviews = interviewRepository.findByMemberIdWithPage(pageable, memberId);
		return PageResponse.of(interviews, InterviewMypageResponse::from);
	}

	public Interview findById(Long id) {
		return interviewRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
	}

	public boolean existsByIdAndStatus(Long interviewId, InterviewStatus status) {
		return interviewRepository.existsByIdAndStatus(interviewId, status);
	}

	public Interview findByVideoId(Long videoId) {
		return interviewRepository.findByVideoId(videoId)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
	}
	
	private Answer createAnswer(String answerContent, Integer processingTime, Interview interview, Question question) {
		AnswerCreateRequest answerCreateRequest = new AnswerCreateRequest(answerContent, processingTime);
		return answerService.createAnswer(answerCreateRequest, question, interview);
	}

	private Question createQuestion(String questionContent, Question firstQuestion) {
		TailQuestionSaveRequest tailQuestionSaveRequest = new TailQuestionSaveRequest(
			questionContent, firstQuestion.getId(), firstQuestion.getCategoryIds());
		return questionService.saveTailQuestion(tailQuestionSaveRequest);
	}

	private List<QuestionAndAnswerResponse> createQuestionAndAnswerResponses(Interview interview) {// TODO: N+1 문제 해결
		return interview.getInterviewQuestions().stream()
			.map(interviewQuestion -> {
				Question question = interviewQuestion.getQuestion();
				Answer answer = answerService.findByInterviewAndQuestion(interview, question).orElse(null);
				return QuestionAndAnswerResponse.of(question, answer);
			})
			.toList();
	}

	private void validateAnswerCount(Interview interview) {
		int answerCount = interview.getAnswers().size();
		if (answerCount >= interview.getQuestionCount()) {
			throw new CustomException(ErrorCode.TOO_MANY_ANSWERS);
		}
	}

}
