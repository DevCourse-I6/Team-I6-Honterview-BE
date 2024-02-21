package com.i6.honterview.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.Question;

public interface QuestionQueryDslRepository {
	Page<Question> findQuestionsByKeywordAndCategoryNamesWithPage(Pageable pageable, String query,
		List<String> categoryNames, String orderType);

	List<Question> findQuestionsByCategoryNames(List<String> categoryNames);
}
