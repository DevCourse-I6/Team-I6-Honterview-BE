package com.i6.honterview.domain.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.user.entity.Admin;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.dto.request.AdminSignUpRequest;
import com.i6.honterview.domain.user.dto.request.LoginRequest;
import com.i6.honterview.domain.user.dto.response.TokenResponse;
import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.common.exception.SecurityCustomException;
import com.i6.honterview.common.exception.SecurityErrorCode;
import com.i6.honterview.domain.user.repository.AdminRepository;
import com.i6.honterview.domain.user.repository.MemberRepository;
import com.i6.honterview.common.security.auth.UserDetailsImpl;
import com.i6.honterview.common.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisService redisService;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final AdminRepository adminRepository;

	public TokenResponse reissue(String token) {
		Object memberIdObj = redisService.get(token);
		if (memberIdObj == null) {
			throw new SecurityCustomException(SecurityErrorCode.REFRESH_TOKEN_EXPIRED);
		}
		Long memberId = Long.parseLong(redisService.get(token).toString());
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		UserDetailsImpl userDetails = UserDetailsImpl.from(member);
		String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
		String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

		redisService.delete(token);
		redisService.saveRefreshToken(refreshToken, memberId);
		return new TokenResponse(accessToken, refreshToken);
	}

	public void logout(String refreshToken, String authorizationToken, Long id) {
		if (redisService.hasKey(refreshToken)) {
			Long userIdByRefresh = Long.valueOf(redisService.get(refreshToken).toString());
			if (userIdByRefresh.equals(id)) {
				String accessToken = jwtTokenProvider.getTokenBearer(authorizationToken);
				redisService.delete(refreshToken);
				redisService.saveBlackList(accessToken, "accessToken");
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
			String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
			redisService.saveRefreshToken(refreshToken, userDetails.getId());

			return new TokenResponse(accessToken, refreshToken);
		} catch (AuthenticationException e) {
			throw new CustomException(ErrorCode.ID_PASSWORD_MATCH_FAIL);
		}
	}
}
