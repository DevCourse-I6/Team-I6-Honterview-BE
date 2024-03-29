package com.i6.honterview.domain.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.common.exception.SecurityCustomException;
import com.i6.honterview.common.exception.SecurityErrorCode;
import com.i6.honterview.common.security.auth.UserDetailsImpl;
import com.i6.honterview.common.security.jwt.JwtTokenProvider;
import com.i6.honterview.domain.user.dto.request.AdminSignUpRequest;
import com.i6.honterview.domain.user.dto.request.LoginRequest;
import com.i6.honterview.domain.user.dto.response.TokenResponse;
import com.i6.honterview.domain.user.entity.Admin;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRedisManager userRedisManager;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final AdminRepository adminRepository;

	public TokenResponse reissue(String refreshToken) {
		checkIsRefreshNull(refreshToken);
		Object memberIdObj = userRedisManager.get(refreshToken);
		if (memberIdObj == null) {
			throw new SecurityCustomException(SecurityErrorCode.REFRESH_TOKEN_EXPIRED);
		}
		Long memberId = Long.parseLong(userRedisManager.get(refreshToken).toString());
		Member member = memberService.findById(memberId);

		UserDetailsImpl userDetails = UserDetailsImpl.from(member);
		String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
		String newRefreshToken = jwtTokenProvider.generateRefreshToken();

		userRedisManager.delete(refreshToken);
		userRedisManager.saveRefreshToken(newRefreshToken, memberId);
		return new TokenResponse(newAccessToken, newRefreshToken);
	}

	public void logout(String refreshToken, String authorizationToken, Long id) {
		checkIsRefreshNull(refreshToken);
		if (userRedisManager.existsRefresh(refreshToken)) {
			Long userIdByRefresh = Long.valueOf(userRedisManager.get(refreshToken).toString());
			if (userIdByRefresh.equals(id)) {
				String accessToken = jwtTokenProvider.getTokenBearer(authorizationToken);
				userRedisManager.delete(refreshToken);
				userRedisManager.saveBlackList(accessToken, "accessToken");
			} else {
				throw new SecurityCustomException(SecurityErrorCode.LOGOUT_FORBIDDEN);
			}
		} else {
			throw new SecurityCustomException(SecurityErrorCode.REFRESH_TOKEN_EXPIRED);
		}
	}

	public Long signUp(AdminSignUpRequest request) {
		if (adminRepository.existsByEmail(request.email())) {
			throw new CustomException(ErrorCode.DUPLICATED_MEMBER_EMAIL);
		}
		Admin admin = adminRepository.save(request.toEntity(passwordEncoder));
		return admin.getId();
	}

	public TokenResponse adminLogin(LoginRequest request) {
		try {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				request.email(),
				request.password()
			);

			Authentication authenticate = authenticationManager.authenticate(authentication);
			SecurityContextHolder.getContext().setAuthentication(authenticate);

			UserDetailsImpl userDetails = (UserDetailsImpl)authenticate.getPrincipal();
			String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
			String refreshToken = jwtTokenProvider.generateRefreshToken();
			userRedisManager.saveRefreshToken(refreshToken, userDetails.getId());

			return new TokenResponse(accessToken, refreshToken);
		} catch (AuthenticationException e) {
			throw new CustomException(ErrorCode.ID_PASSWORD_MATCH_FAIL);
		}
	}

	private void checkIsRefreshNull(String refreshToken) {
		if (refreshToken == null) {
			throw new SecurityCustomException(SecurityErrorCode.REFRESH_NOT_EXIST);
		}
	}
}
