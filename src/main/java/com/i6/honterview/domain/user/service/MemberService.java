package com.i6.honterview.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.user.dto.request.MemberUpdateRequest;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	public Member findById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	public String updateNickname(Long memberId, MemberUpdateRequest request) {
		Member member = findById(memberId);
		member.changeNickname(request.nickname());
		return member.getNickname();
	}
}
