package com.i6.honterview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

}
