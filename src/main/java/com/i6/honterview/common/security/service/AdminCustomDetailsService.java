package com.i6.honterview.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.i6.honterview.domain.user.entity.Admin;
import com.i6.honterview.common.exception.SecurityCustomException;
import com.i6.honterview.common.exception.SecurityErrorCode;
import com.i6.honterview.domain.user.repository.AdminRepository;
import com.i6.honterview.common.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCustomDetailsService implements UserDetailsService {
	private final AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByEmail(username)
			.orElseThrow(() -> new SecurityCustomException(SecurityErrorCode.FORBIDDEN));
		return UserDetailsImpl.from(admin);
	}
}
