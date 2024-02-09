package com.i6.honterview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
