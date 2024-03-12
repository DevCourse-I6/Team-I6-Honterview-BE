package com.i6.honterview.common.security.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.i6.honterview.domain.user.entity.Admin;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.entity.Provider;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
	private Long id;
	private Provider provider;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	@Builder
	public UserDetailsImpl(Long id, Collection<? extends GrantedAuthority> authorities, Provider provider,
		String password) {
		this.id = id;
		this.authorities = authorities;
		this.provider = provider;
		this.password = password;
	}

	public static UserDetailsImpl from(Member member) {
		List<GrantedAuthority> authorities = member.getRole() != null ?
			List.of(new SimpleGrantedAuthority(member.getRole().name()))
			: null;
		return UserDetailsImpl.builder()
			.id(member.getId())
			.authorities(authorities)
			.provider(member.getProvider())
			.build();
	}

	public static UserDetailsImpl from(Admin admin) {
		List<GrantedAuthority> authorities = admin.getRole() != null ?
			List.of(new SimpleGrantedAuthority(admin.getRole().name()))
			: null;
		return UserDetailsImpl.builder()
			.id(admin.getId())
			.authorities(authorities)
			.password(admin.getPassword())
			.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
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
