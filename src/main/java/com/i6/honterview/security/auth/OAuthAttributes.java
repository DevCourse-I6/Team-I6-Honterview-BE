package com.i6.honterview.security.auth;

import java.util.HashMap;
import java.util.Map;

import com.i6.honterview.domain.enums.Provider;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {

	private String attributeKey; // 사용자 속성의 키 값
	private String email;
	private Provider provider;
	private String providerId;
	private String nickname;

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
			case NAVER -> ofNaver(nameAttributeName, attributes);
			case GITHUB -> ofGithub(nameAttributeName, attributes);
			default -> throw new IllegalArgumentException("지원되지 않는 프로바이더 타입: " + provider);
		};
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) response.get("profile");
		String nickname = (String) profile.get("nickname");
		return OAuthAttributes.builder()
			.nickname(nickname)
			.email((String)response.get("email"))
			.provider(Provider.KAKAO)
			.providerId(String.valueOf(attributes.get("id")))
			.attributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nickname((String) attributes.get("name"))
			.email((String)attributes.get("email"))
			.provider(Provider.GOOGLE)
			.providerId((String)attributes.get("sub"))
			.attributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("response");
		return OAuthAttributes.builder()
			.nickname((String) response.get("name"))
			.email((String) response.get("email"))
			.provider(Provider.NAVER)
			.providerId(String.valueOf(response.get("id")))
			.attributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
		System.out.println(attributes.toString());
		return OAuthAttributes.builder()
			.nickname((String)attributes.get("login"))
			.email((String)attributes.get("email"))
			.provider(Provider.GITHUB)
			.providerId(String.valueOf(attributes.get("id")))
			.attributeKey(userNameAttributeName)
			.build();
	}

	public Map<String, Object> convertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", attributeKey);
		map.put("nickname", nickname);
		map.put("email", email);
		map.put("provider", provider);
		map.put(attributeKey, providerId);
		return map;
	}
}
