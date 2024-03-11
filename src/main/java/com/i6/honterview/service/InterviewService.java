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
import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.Video;
import com.i6.honterview.domain.enums.InterviewStatus;
import com.i6.honterview.dto.request.AnswerCreateRequest;
import com.i6.honterview.dto.request.AnswerVisibilityUpdateRequest;
import com.i6.honterview.dto.request.InterviewCreateRequest;
import com.i6.honterview.dto.request.QuestionAnswerCreateRequest;
import com.i6.honterview.dto.request.TailQuestionSaveRequest;
import com.i6.honterview.dto.response.AnswersVisibilityUpdateResponse;
import com.i6.honterview.dto.response.InterviewInfoResponse;
import com.i6.honterview.dto.response.QuestionAndAnswerResponse;
import com.i6.honterview.dto.response.QuestionAnswerCreateResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.AnswerRepository;
import com.i6.honterview.repository.InterviewRepository;
import com.i6.honterview.repository.MemberRepository;
import com.i6.honterview.repository.QuestionRepository;
import com.i6.honterview.repository.VideoRepository;

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
	private final VideoRepository videoRepository;

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
			interview.changeStatus(InterviewStatus.COMPLETED);
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

		validateAnswerCount(interview);

		Question firstQuestion = interview.findFirstQuestion();
		Question question;
		if (interview.getAnswers().isEmpty()) { // 첫번째 질문 -> 답변만 생성
			question = firstQuestion;
		} else { // 이후 꼬리질문 -> 질문, 답변 생성
			question = createQuestion(request.questionContent(), firstQuestion);
			interview.addQuestion(question);
		}

		Video video = getVideo(request);
		Answer answer = createAnswer(request.answerContent(), interview, question, video);
		return QuestionAnswerCreateResponse.of(question, answer);
	}

	private Video getVideo(QuestionAnswerCreateRequest request) {
		if (request.videoId() != null) {
			return videoRepository.findById(request.videoId())
				.orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND))
				.changeProcessingTime(request.processingTime());
		}
		return null;
	}

	private void validateAnswerCount(Interview interview) {
		int answerCount = interview.getAnswers().size();
		if (answerCount >= interview.getQuestionCount()) {
			throw new CustomException(ErrorCode.TOO_MANY_ANSWERS);
		}
	}

	public InterviewInfoResponse getInterviewInfo(Long id) {
		Interview interview = interviewRepository.findByIdWithInterviewQuestions(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));
		List<QuestionAndAnswerResponse> questionsAndAnswers = createQuestionAndAnswerResponses(interview);
		return InterviewInfoResponse.of(interview, questionsAndAnswers);
	}

	private Answer createAnswer(String answerContent, Interview interview, Question question, Video video) {
		AnswerCreateRequest answerCreateRequest = new AnswerCreateRequest(answerContent);
		return answerService.createAnswer(answerCreateRequest, question, interview, video);
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
				Answer answer = answerRepository.findByInterviewAndQuestion(interview, question).orElse(null);
				return QuestionAndAnswerResponse.of(question, answer); //TODO: processingTime 조회 로직 추가
			})
			.collect(Collectors.toList());
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
