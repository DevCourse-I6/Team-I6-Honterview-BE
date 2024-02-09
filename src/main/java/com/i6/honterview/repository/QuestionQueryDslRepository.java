package com.i6.honterview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.Question;

public interface QuestionQueryDslRepository {
	Page<Question> findQuestionsByKeywordWithPage(Pageable pageable, String query);
}
