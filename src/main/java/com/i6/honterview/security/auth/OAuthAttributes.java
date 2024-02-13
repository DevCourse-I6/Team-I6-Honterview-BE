package com.i6.honterview.security.auth;

import java.util.HashMap;
import java.util.Map;

import com.i6.honterview.domain.enums.Provider;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String attributeKey; // 사용자 속성의 키 값
	private String email;
	private Provider provider;
	private String providerId;
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
			case GOOGLE -> ofGoogle(nameAttributeName, attributes);
			default -> throw new IllegalArgumentException("지원되지 않는 프로바이더 타입: " + provider);
		};
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) response.get("profile");
		String nickname = (String) profile.get("nickname");
		return OAuthAttributes.builder()
			.name(nickname)
			.email((String)response.get("email"))
			.attributes(attributes)
			.provider(Provider.KAKAO)
			.providerId(String.valueOf(attributes.get("id")))
			.attributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		String nickname = (String) attributes.get("name");
		return OAuthAttributes.builder()
			.name(nickname)
			.email((String)attributes.get("email"))
			.attributes(attributes)
			.provider(Provider.GOOGLE)
			.providerId((String)attributes.get("sub"))
			.attributeKey(userNameAttributeName)
			.build();
	}

	public Map<String, Object> convertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", attributeKey);
		map.put("nickname", name);
		map.put("email", email);
		map.put("provider", provider);
		map.put("sub", providerId);
		return map;
	}
}
