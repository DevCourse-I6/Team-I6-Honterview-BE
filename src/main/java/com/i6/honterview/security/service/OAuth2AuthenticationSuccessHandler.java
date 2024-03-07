package com.i6.honterview.security.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.enums.Provider;
import com.i6.honterview.domain.enums.Role;
import com.i6.honterview.repository.MemberRepository;
import com.i6.honterview.repository.RedisRepository;
import com.i6.honterview.security.auth.UserDetailsImpl;
import com.i6.honterview.security.jwt.JwtTokenProvider;
import com.i6.honterview.util.HttpResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final String CHECKING_EXIST_KEY = "exist";
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final RedisRepository redisRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User)authentication.getPrincipal();
		Map<String, Object> attributes = defaultOAuth2User.getAttributes();
		String email = (String)attributes.get("email");
		Provider provider = (Provider)attributes.get("provider");
		String nickname = (String)attributes.get("nickname");

		Map<String, Object> body = new HashMap<>();
		body.put(CHECKING_EXIST_KEY, true);

		Member member = memberRepository.findByEmailAndProvider(email, provider).orElseGet(() -> {
				body.put(CHECKING_EXIST_KEY, false);
				return Member.builder()
					.provider(provider)
					.nickname(nickname)
					.email(email)
					.role(Role.ROLE_USER)
					.build();
			});
		member.updateLastLoginAt();
		member = memberRepository.save(member);

		UserDetailsImpl userDetails = UserDetailsImpl.from(member);

		String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
		String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
		redisRepository.saveRefreshToken(refreshToken, member.getId());

		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
		// accessTokenCookie.setSecure(true);
		// accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setPath("/");
		accessTokenCookie.setDomain("honterview.site");
		response.addCookie(accessTokenCookie);

		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		// refreshTokenCookie.setSecure(true);
		// refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setPath("/");
		accessTokenCookie.setDomain("honterview.site");
		response.addCookie(refreshTokenCookie);

		response.sendRedirect("http://localhost:3000");
		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, body);
	}
}
