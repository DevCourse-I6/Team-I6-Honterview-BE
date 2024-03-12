package com.i6.honterview.domain.question.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.question.entity.QuestionHeart;

public interface QuestionHeartRepository extends JpaRepository<QuestionHeart, Long> {

	Optional<QuestionHeart> findByQuestionAndMemberId(Question question, Long memberId);
}
