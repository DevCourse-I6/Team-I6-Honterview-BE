package com.i6.honterview.security.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Member;
import com.i6.honterview.repository.MemberRepository;
import com.i6.honterview.security.auth.OAuth2UserImpl;
import com.i6.honterview.security.auth.OAuthAttributes;

import lombok.RequiredArgsConstructor;

// 소셜로부터 받은 userReqeust에 대한 후처리
@Service
@Transactional
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);

		// 공급자(Google, Naver, ...) ID
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// nameAttributeKey (식별자)
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
			oauth2User.getAttributes());

		Member member = saveOrUpdate(attributes);

		return new OAuth2UserImpl(
			Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
			oauth2User.getAttributes(),
			attributes.getNameAttributeKey(),
			member);
	}

	private Member saveOrUpdate(OAuthAttributes attributes) {
		return memberRepository.findByEmail(attributes.getEmail())
			.orElseGet(() -> {
				Member newMember = attributes.toEntity();
				return memberRepository.save(newMember);
			});
	}
}
