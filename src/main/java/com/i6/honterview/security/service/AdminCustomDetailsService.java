package com.i6.honterview.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.i6.honterview.domain.Admin;
import com.i6.honterview.exception.SecurityCustomException;
import com.i6.honterview.exception.SecurityErrorCode;
import com.i6.honterview.repository.AdminRepository;
import com.i6.honterview.security.auth.UserDetailsImpl;

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
