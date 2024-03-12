package com.i6.honterview.domain.question.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i6.honterview.domain.question.entity.QuestionBookmark;

public interface QuestionBookmarkRepository extends JpaRepository<QuestionBookmark, Long> {

	@Query("SELECT qb FROM QuestionBookmark qb WHERE qb.question.id = :questionId AND qb.member.id = :memberId")
	Optional<QuestionBookmark> findByQuestionIdAndMemberId(@Param("questionId") Long questionId,
		@Param("memberId") Long memberId);

}
