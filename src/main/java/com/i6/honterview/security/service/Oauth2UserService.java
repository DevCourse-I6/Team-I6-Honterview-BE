package com.i6.honterview.security.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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

		OAuthAttributes oAuth2Attribute = OAuthAttributes.of(registrationId, userNameAttributeName,
			oauth2User.getAttributes());

		Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

		Optional<Member> findMember = memberRepository.findByEmail(oAuth2Attribute.getEmail());
		if (findMember.isEmpty()) {
			memberAttribute.put("exist", false);

			// TODO : 만약 최초로그인인 경우 회원가입 페이지로 이동 -> 그 후 save 하도록 수정하기
			Member member = memberRepository.save(oAuth2Attribute.toMemberEntity());

			return new OAuth2UserImpl(
				Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
				memberAttribute,
				oAuth2Attribute.getAttributeKey(),
				member
			);
		}

		Member member = findMember.get();
		memberAttribute.put("exist", true);

		return new OAuth2UserImpl(
			Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
			memberAttribute,
			oAuth2Attribute.getAttributeKey(),
			member);
	}
}
