package com.i6.honterview.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.redis.RefreshToken;
import com.i6.honterview.dto.request.ReissueTokenRequest;
import com.i6.honterview.dto.response.TokenResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.MemberRepository;
import com.i6.honterview.repository.RefreshTokenRepository;
import com.i6.honterview.security.auth.UserDetailsImpl;
import com.i6.honterview.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponse reissue(ReissueTokenRequest request) {
		RefreshToken oldRefreshToken = refreshTokenRepository.findById(request.refreshToken())
			.orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED));

		Member member = memberRepository.findByEmail(oldRefreshToken.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		UserDetailsImpl userDetails = UserDetailsImpl.from(member);
		String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
		String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

		refreshTokenRepository.delete(oldRefreshToken);
		refreshTokenRepository.save(new RefreshToken(refreshToken, accessToken, userDetails.getEmail()));
		return new TokenResponse(accessToken, refreshToken);
	}
}
