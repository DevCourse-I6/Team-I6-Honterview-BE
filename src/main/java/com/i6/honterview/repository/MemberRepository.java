package com.i6.honterview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.enums.Provider;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmailAndProvider(String email, Provider provider);
}
