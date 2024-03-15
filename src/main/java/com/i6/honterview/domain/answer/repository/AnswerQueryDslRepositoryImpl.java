package com.i6.honterview.domain.answer.repository;

import static com.i6.honterview.domain.answer.entity.QAnswer.*;
import static com.i6.honterview.domain.answer.entity.QAnswerHeart.*;
import static com.i6.honterview.domain.user.entity.QMember.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.question.entity.Question;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnswerQueryDslRepositoryImpl implements AnswerQueryDslRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Answer> findByQuestionIdWithMemberAndHearts(Long questionId, Pageable pageable) {
		List<Answer> answers = queryFactory
			.selectFrom(answer)
			.leftJoin(answer.member, member).fetchJoin()
			.leftJoin(answer.answerHearts, answerHeart).fetchJoin()
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
