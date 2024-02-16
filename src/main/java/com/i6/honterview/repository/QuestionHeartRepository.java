package com.i6.honterview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.QuestionHeart;

public interface QuestionHeartRepository extends JpaRepository<QuestionHeart, Long> {

	Optional<QuestionHeart> findByQuestionAndMemberId(Question question, Long memberId);
}
