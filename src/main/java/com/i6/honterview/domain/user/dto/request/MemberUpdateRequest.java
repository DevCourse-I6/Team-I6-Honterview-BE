package com.i6.honterview.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "마이페이지 닉네임 수정 요청")
public record MemberUpdateRequest(

	@Schema(description = "닉네임", example = "노력하는 감자")
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]{2,20}$", message = "닉네임은 영어, 숫자, 한글로만 구성해주세요.")
	String nickname

) {
}
