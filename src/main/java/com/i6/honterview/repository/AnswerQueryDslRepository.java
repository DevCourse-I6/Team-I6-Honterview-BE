package com.i6.honterview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.Answer;

public interface AnswerQueryDslRepository {
	Page<Answer> findByQuestionIdWithMember(Long questionId, Pageable pageable);
}
