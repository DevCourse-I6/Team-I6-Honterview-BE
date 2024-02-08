package com.i6.honterview.security.auth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.i6.honterview.domain.Member;

import lombok.Getter;

@Getter
public class OAuth2UserImpl extends DefaultOAuth2User {

	private final Member member;

	public OAuth2UserImpl(Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes, String nameAttributeKey, Member member) {
		super(authorities, attributes, nameAttributeKey);
		this.member = member;
	}
}
