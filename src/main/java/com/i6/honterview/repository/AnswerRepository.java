package com.i6.honterview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerQueryDslRepository {
}
