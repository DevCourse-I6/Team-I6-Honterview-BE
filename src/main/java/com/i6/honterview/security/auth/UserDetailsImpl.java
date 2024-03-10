package com.i6.honterview.security.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.i6.honterview.domain.Admin;
import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.enums.Provider;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
	private Long id;
	private Provider provider;
	private Collection<? extends GrantedAuthority> authorities;

	@Builder
	public UserDetailsImpl(Long id, Collection<? extends GrantedAuthority> authorities, Provider provider) {
		this.id = id;
		this.authorities = authorities;
		this.provider = provider;
	}

	public static UserDetailsImpl from(Member member) {
		List<GrantedAuthority> authorities = member.getRole() != null ?
			List.of(new SimpleGrantedAuthority(member.getRole().name()))
			: null;
		return new UserDetailsImpl(
			member.getId(),
			authorities,
			member.getProvider()
		);
	}

	public static UserDetailsImpl from(Admin admin) {
		List<GrantedAuthority> authorities = admin.getRole() != null ?
			List.of(new SimpleGrantedAuthority(admin.getRole().name()))
			: null;
		return UserDetailsImpl.builder()
			.id(admin.getId())
			.authorities(authorities)
			.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return id.toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
