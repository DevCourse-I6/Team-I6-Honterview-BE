package com.i6.honterview.repository;

import static com.i6.honterview.domain.QAnswer.*;
import static com.i6.honterview.domain.QMember.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Question;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnswerQueryDslRepositoryImpl implements AnswerQueryDslRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Answer> findByQuestionIdWithMember(Long questionId, Pageable pageable) {
		List<Answer> answers = queryFactory
			.selectFrom(answer)
			.leftJoin(answer.member, member).fetchJoin()
			.where(answer.question.id.eq(questionId))
			.orderBy(answer.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(answer.count())
			.from(answer)
			.where(answer.question.id.eq(questionId));
		return PageableExecutionUtils.getPage(answers, pageable, countQuery::fetchOne);
	}

	@Override
	public boolean existsByInterviewAndQuestion(Interview interview, Question question) {
		return queryFactory.selectFrom(answer)
			.where(answer.interview.eq(interview)
				.and(answer.question.eq(question)))
			.fetchFirst() != null;
	}
}
