package com.i6.honterview.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Question;
import com.i6.honterview.dto.request.AnswerCreateRequest;
import com.i6.honterview.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerRepository answerRepository;

	public Answer createAnswer(AnswerCreateRequest request, Question question, Interview interview) {
		Answer answer = answerRepository.save(request.toEntity(question, interview));
		return answer;
	}
}
