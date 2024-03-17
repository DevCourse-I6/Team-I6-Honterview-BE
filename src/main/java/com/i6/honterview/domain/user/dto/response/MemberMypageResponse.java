package com.i6.honterview.domain.user.dto.response;

import com.i6.honterview.domain.user.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마이페이지 내 정보 응답")
public record MemberMypageResponse(
	@Schema(description = "회원 ID", example = "123")
	Long id,

	@Schema(description = "닉네임", example = "용감한 사자")
	String nickname,

	@Schema(description = "이메일", example = "member1@example.com")
	String email
) {
	public static MemberMypageResponse from(Member member) {
		return new MemberMypageResponse(
			member.getId(),
			member.getNickname(),
			member.getEmail()
		);
	}
}
