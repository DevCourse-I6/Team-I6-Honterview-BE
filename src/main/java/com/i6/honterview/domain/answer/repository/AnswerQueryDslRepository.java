package com.i6.honterview.domain.answer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.question.entity.Question;

public interface AnswerQueryDslRepository {
	Page<Answer> findByQuestionIdWithMemberAndHearts(Long questionId, Pageable pageable);

	boolean existsByInterviewAndQuestion(Interview interview, Question question);
}
