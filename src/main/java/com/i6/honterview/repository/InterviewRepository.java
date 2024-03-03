package com.i6.honterview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.enums.InterviewStatus;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

	@Query("SELECT i FROM Interview i LEFT JOIN FETCH i.interviewQuestions WHERE i.id = :interviewId")
	Optional<Interview> findByIdWithInterviewQuestions(@Param("interviewId") Long interviewId);

	boolean existsByIdAndStatus(Long id, InterviewStatus status);
}
