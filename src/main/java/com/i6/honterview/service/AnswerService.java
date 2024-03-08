package com.i6.honterview.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.Video;
import com.i6.honterview.dto.request.AnswerCreateRequest;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.AnswerRepository;

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
