package com.i6.honterview.domain.interview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.interview.entity.Interview;

public interface InterviewQueryDslRepository {
	Page<Interview> findByMemberIdWithPage(Pageable pageable, Long memberId);
}
