package com.i6.honterview.domain.user.dto.response;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginUserResponse(

	@Schema(description = "로그인한 사용자 id", example = "123")
	Long id,

	@Schema(description = "로그인한 사용자 권한", example = "ROLE_USER")
	Collection<? extends GrantedAuthority> role
	) {
}
