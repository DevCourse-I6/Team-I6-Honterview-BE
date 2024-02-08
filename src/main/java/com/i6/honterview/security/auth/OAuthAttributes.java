package com.i6.honterview.security.auth;

import java.util.Map;

import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.enums.Provider;
import com.i6.honterview.domain.enums.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String email;
	private Provider provider;
	private String name;

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		Provider provider = Provider.getProviderFromString(registrationId);
		return ofProviderType(provider, userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofProviderType(Provider provider, String userNameAttributeName,
		Map<String, Object> attributes) {
		return switch (provider) {
			case KAKAO -> ofKakao(userNameAttributeName, attributes);
			default -> throw new IllegalArgumentException("지원되지 않는 프로바이더 타입: " + provider);
		};
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("kakao_account");
		return buildCommonAttributes(Provider.KAKAO, userNameAttributeName, response);
	}

	private static OAuthAttributes buildCommonAttributes(Provider provider, String userNameAttributeName,
		Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.email((String)attributes.get("email"))
			.attributes(attributes)
			.provider(provider)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	public Member toEntity() {
		return Member.builder()
			.provider(provider)
			.email(email)
			.role(Role.ROLE_USER)
			.build();
	}
}
