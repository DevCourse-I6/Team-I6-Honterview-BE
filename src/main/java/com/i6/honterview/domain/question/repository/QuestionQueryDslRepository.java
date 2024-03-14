package com.i6.honterview.domain.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.question.entity.Question;

public interface QuestionQueryDslRepository {
	Page<Question> findQuestionsByKeywordAndCategoryNamesWithPage(Pageable pageable, String query,
		List<String> categoryNames, String orderType);

	List<Question> findQuestionsByCategoryNames(List<String> categoryNames);

	Page<Question> findByMemberIdWithPage(Pageable pageable, Long memberId);

	Optional<Question> findQuestionByIdWithCategoriesAndHearts(Long id);
}
