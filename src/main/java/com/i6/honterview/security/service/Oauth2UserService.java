package com.i6.honterview.security.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.security.auth.OAuthAttributes;

import lombok.RequiredArgsConstructor;

// 소셜로부터 받은 userReqeust에 대한 후처리
@Service
@Transactional
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);

		// 공급자(Google, Naver, ...) ID
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// nameAttributeKey 식별자(google-sub, kakao-id, ...)
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		OAuthAttributes oAuth2Attribute = OAuthAttributes.of(registrationId, userNameAttributeName,
			oauth2User.getAttributes());
		Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

		return new DefaultOAuth2User(Collections.emptyList(), memberAttribute, userNameAttributeName);
	}
}
