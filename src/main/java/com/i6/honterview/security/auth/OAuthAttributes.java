package com.i6.honterview.security.auth;

import java.util.HashMap;
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
	private String attributeKey; // 사용자 속성의 키 값
	private String email;
	private Provider provider;
	private String name;

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		Provider provider = Provider.getProviderFromString(registrationId);
		return ofProviderType(provider, userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofProviderType(Provider provider, String nameAttributeName,
		Map<String, Object> attributes) {
		return switch (provider) {
			case KAKAO -> ofKakao(nameAttributeName, attributes);
			default -> throw new IllegalArgumentException("지원되지 않는 프로바이더 타입: " + provider);
		};
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) response.get("profile");
		String nickname = (String) profile.get("nickname");
		return buildCommonAttributes(Provider.KAKAO, userNameAttributeName, response, nickname);
	}

	private static OAuthAttributes buildCommonAttributes(Provider provider, String userNameAttributeName,
		Map<String, Object> attributes, String nickname) {
		return OAuthAttributes.builder()
			.name(nickname)
			.email((String)attributes.get("email"))
			.attributes(attributes)
			.provider(provider)
			.attributeKey(userNameAttributeName)
			.build();
	}

	public Map<String, Object> convertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", attributeKey);
		map.put("nickname", name);
		map.put("email", email);
		map.put("provider", provider);
		return map;
	}

	public Member toMemberEntity() {
		return Member.builder()
			.provider(provider)
			.nickname(name)
			.email(email)
			.role(Role.ROLE_USER)
			.build();
	}
}
