package com.i6.honterview.domain.user.dto.request;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.i6.honterview.domain.user.entity.Admin;
import com.i6.honterview.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdminSignUpRequest(
	@Schema(description = "이메일", example = "admin@gmail.com")
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "이메일 형식에 맞게 입력해주세요.")
	String email,

	@Schema(description = "비밀번호", example = "admin1234!")
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	String password,

	@Schema(description = "이름", example = "홍길동")
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	String name
) {
	public Admin toEntity(PasswordEncoder encoder) {
		return new Admin(
			name,
			email,
			encoder.encode(password),
			Role.ROLE_ADMIN
		);
	}
}
