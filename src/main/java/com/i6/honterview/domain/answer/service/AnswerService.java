package com.i6.honterview.domain.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.interview.entity.Video;
import com.i6.honterview.domain.answer.dto.request.AnswerCreateRequest;
import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.answer.repository.AnswerRepository;

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
}
