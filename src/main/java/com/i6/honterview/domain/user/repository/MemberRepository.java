package com.i6.honterview.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.entity.Provider;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmailAndProvider(String email, Provider provider);
}
