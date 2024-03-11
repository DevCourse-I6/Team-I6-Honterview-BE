package com.i6.honterview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.i6.honterview.domain.Interview;

public interface InterviewQueryDslRepository {
	Page<Interview> findByMemberIdWithPage(Pageable pageable, Long memberId);
}
