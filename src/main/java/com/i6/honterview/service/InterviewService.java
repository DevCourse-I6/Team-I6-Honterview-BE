package com.i6.honterview.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.enums.InterviewStatus;
import com.i6.honterview.dto.request.AnswerCreateRequest;
import com.i6.honterview.dto.request.InterviewCompleteRequest;
import com.i6.honterview.dto.request.InterviewCreateRequest;
import com.i6.honterview.dto.request.QuestionAnswerCreateRequest;
import com.i6.honterview.dto.request.QuestionCreateRequest;
import com.i6.honterview.dto.response.InterviewCompleteResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
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

	public InterviewCompleteResponse completeInterviewAndSaveAnswers(Long id, InterviewCompleteRequest request) {
		Interview interview = interviewRepository.findByIdWithMember(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		Question firstQuestion = findFirstQuestion(request.questionAnswerRequest())
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		for (QuestionAnswerCreateRequest req : request.questionAnswerRequest()) {
			Question question = null;
			if (req.sequence() != 1) {
				question = createQuestion(req, firstQuestion.getId(), request.catogoryIds());
			} else {
				question = firstQuestion;
			}
			interview.addQuestion(question);
			createAnswer(req, question, interview);
		}

		interview.completeInterview();
		return InterviewCompleteResponse.from(interview);
	}

	private Question createQuestion(QuestionAnswerCreateRequest req, Long parentId, List<Long> categoryIds) {
		QuestionCreateRequest questionCreateRequest = new QuestionCreateRequest(
			req.questionContent(), parentId, req.visibility(), categoryIds);
		return questionService.createQuestion(questionCreateRequest);
	}

	private void createAnswer(QuestionAnswerCreateRequest req, Question question, Interview interview) {
		AnswerCreateRequest answerCreateRequest = new AnswerCreateRequest(req.answerContent(), req.visibility());
		answerService.createAnswer(answerCreateRequest, question, interview);
	}

	private Optional<Question> findFirstQuestion(List<QuestionAnswerCreateRequest> requests) {
		return requests.stream()
			.filter(req -> req.sequence() == 1)
			.findFirst()
			.map(req -> questionRepository.findById(req.questionId())
				.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND)));
	}
}
