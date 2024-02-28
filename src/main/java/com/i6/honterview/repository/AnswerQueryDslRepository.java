package com.i6.honterview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Question;

public interface AnswerQueryDslRepository {
	Page<Answer> findByQuestionIdWithMember(Long questionId, Pageable pageable);

	boolean existsByInterviewAndQuestion(Interview interview, Question question);
}
