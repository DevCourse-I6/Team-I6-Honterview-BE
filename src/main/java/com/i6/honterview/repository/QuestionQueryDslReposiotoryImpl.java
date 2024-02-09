package com.i6.honterview.repository;

import static com.i6.honterview.domain.QQuestion.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.i6.honterview.domain.Question;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionQueryDslReposiotoryImpl implements QuestionQueryDslRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Question> findQuestionsByKeywordWithPage(Pageable pageable, String query) {
		BooleanExpression searchCondition = StringUtils.hasText(query) ?
			question.content.contains(query) : null;

		List<Question> questions = queryFactory
			.selectFrom(question)
			.where(searchCondition)
			.orderBy(question.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(question.count())
			.from(question)
			.where(searchCondition);

		return PageableExecutionUtils.getPage(questions, pageable, countQuery::fetchOne);
	}
}
