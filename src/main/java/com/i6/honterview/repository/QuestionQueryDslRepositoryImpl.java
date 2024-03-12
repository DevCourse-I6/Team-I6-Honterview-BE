package com.i6.honterview.repository;

import static com.i6.honterview.domain.QCategory.*;
import static com.i6.honterview.domain.QQuestion.*;
import static com.i6.honterview.domain.QQuestionBookmark.*;
import static com.i6.honterview.domain.QQuestionCategory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.i6.honterview.domain.Question;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionQueryDslRepositoryImpl implements QuestionQueryDslRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Question> findQuestionsByKeywordAndCategoryNamesWithPage(Pageable pageable, String query,
		List<String> categoryNames, String orderType) {
		// 조건 생성 - 카테고리 ID 조회 조건, 검색 조건 및 결합
		BooleanExpression categoryCondition = createCategoryCondition(categoryNames);
		BooleanExpression searchCondition = createSearchCondition(query);
		BooleanExpression combinedCondition =
			searchCondition != null ? searchCondition.and(categoryCondition) : categoryCondition;

		// 질문 조회
		List<Question> questions = fetchQuestions(pageable, combinedCondition, orderType);

		// 카운트 쿼리
		JPAQuery<Long> countQuery = fetchQuestionCount(combinedCondition);

		return PageableExecutionUtils.getPage(questions, pageable, countQuery::fetchOne);
	}

	@Override
	public List<Question> findQuestionsByCategoryNames(List<String> categoryNames) {
		BooleanExpression categoryCondition = createCategoryCondition(categoryNames);
		if (categoryCondition == null) {
			return new ArrayList<>();
		}

		return queryFactory
			.selectFrom(question)
			.where(categoryCondition)
			.fetch();
	}

	private BooleanExpression createCategoryCondition(List<String> categoryNames) {
		if (CollectionUtils.isEmpty(categoryNames)) {
			return null;
		}
		List<Long> categoryIds = queryFactory
			.select(category.id)
			.from(category)
			.where(category.categoryName.in(categoryNames))
			.fetch();
		return categoryIds.isEmpty() ? null : question.questionCategories.any().category.id.in(categoryIds);
	}

	private BooleanExpression createSearchCondition(String query) {
		return StringUtils.hasText(query) ? question.content.containsIgnoreCase(query) : null;
	}

	private List<Question> fetchQuestions(Pageable pageable, BooleanExpression condition, String orderType) {
		return queryFactory
			.selectFrom(question)
			.distinct()
			.leftJoin(question.questionCategories, questionCategory).fetchJoin()
			.leftJoin(questionCategory.category, category).fetchJoin()
			.where(condition)
			.orderBy(getOrderSpecifier(orderType))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private OrderSpecifier<?> getOrderSpecifier(String orderType) {
		if ("hearts".equals(orderType)) {
			return question.heartsCount.desc();
		} else if ("recent".equals(orderType)) {
			return question.id.desc();
		}
		throw new CustomException(ErrorCode.ORDER_TYPE_NOT_FOUND);
	}

	private JPAQuery<Long> fetchQuestionCount(BooleanExpression condition) {
		return queryFactory
			.select(question.countDistinct())
			.from(question)
			.leftJoin(question.questionCategories, questionCategory)
			.where(condition);
	}

	public Optional<Question> findQuestionByIdWithCategories(Long id) {
		Question questionWithCategories = queryFactory
			.selectFrom(question)
			.distinct()
			.leftJoin(question.questionCategories, questionCategory).fetchJoin()
			.leftJoin(questionCategory.category, category).fetchJoin()
			.where(question.id.eq(id))
			.fetchFirst();

		return Optional.ofNullable(questionWithCategories);
	}

	@Override
	public Page<Question> findByMemberIdWithPage(Pageable pageable, Long memberId) {

		List<Question> questions = queryFactory
			.select(question)
			.from(questionBookmark)
			.join(questionBookmark.question, question)
			.where(questionBookmark.member.id.eq(memberId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(question.count())
			.from(questionBookmark)
			.join(questionBookmark.question, question)
			.where(questionBookmark.member.id.eq(memberId));

		return PageableExecutionUtils.getPage(questions, pageable, countQuery::fetchOne);
	}

}
