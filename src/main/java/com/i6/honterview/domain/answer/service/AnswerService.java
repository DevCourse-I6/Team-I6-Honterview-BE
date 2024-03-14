package com.i6.honterview.domain.answer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.answer.dto.request.AnswerCreateRequest;
import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.answer.repository.AnswerRepository;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.interview.entity.Video;
import com.i6.honterview.domain.question.entity.Question;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerRepository answerRepository;

	public Answer createAnswer(AnswerCreateRequest request, Question question, Interview interview, Video video) {
		boolean answerExists = answerRepository.existsByInterviewAndQuestion(interview, question);
		if (answerExists) {
			throw new CustomException(ErrorCode.ANSWER_DUPLICATED);
		}
		return answerRepository.save(request.toEntity(question, interview, video));
	}

	public Page<Answer> findByQuestionIdWithMember(Long id, Pageable pageable) {
		return answerRepository.findByQuestionIdWithMember(id, pageable);
	}

	public Answer findByIdWithHearts(Long answerId) {
		return answerRepository.findByIdWithHearts(answerId)
			.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
	}

	public List<Answer> findByInterviewId(Long interviewId) {
		return answerRepository.findByInterviewId(interviewId);
	}

	public Optional<Answer> findByInterviewAndQuestion(Interview interview, Question question) {
		return answerRepository.findByInterviewAndQuestion(interview, question);
	}
}
