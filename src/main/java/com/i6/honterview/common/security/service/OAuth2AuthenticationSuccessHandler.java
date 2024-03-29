package com.i6.honterview.common.security.service;

import static com.i6.honterview.common.util.NicknameGeneratorUtil.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.i6.honterview.common.security.auth.UserDetailsImpl;
import com.i6.honterview.common.security.jwt.JwtTokenProvider;
import com.i6.honterview.common.util.CookieUtil;
import com.i6.honterview.common.util.HttpResponseUtil;
import com.i6.honterview.config.JwtConfig;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.entity.Provider;
import com.i6.honterview.domain.user.entity.Role;
import com.i6.honterview.domain.user.repository.MemberRepository;
import com.i6.honterview.domain.user.service.UserRedisManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final String CHECKING_EXIST_KEY = "exist";
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final UserRedisManager userRedisManager;
	private final JwtConfig jwtConfig;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User)authentication.getPrincipal();
		Map<String, Object> attributes = defaultOAuth2User.getAttributes();
		String email = (String)attributes.get("email");
		Provider provider = (Provider)attributes.get("provider");

		Map<String, Object> body = new HashMap<>();
		body.put(CHECKING_EXIST_KEY, true);

		Member member = memberRepository.findByEmailAndProvider(email, provider).orElseGet(() -> {
			body.put(CHECKING_EXIST_KEY, false);
			return Member.builder()
				.provider(provider)
				.nickname(generateRandomNickname())
				.email(email)
				.role(Role.ROLE_USER)
				.build();
		});
		member.updateLastLoginAt();
		member = memberRepository.save(member);

		UserDetailsImpl userDetails = UserDetailsImpl.from(member);

		String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
		String refreshToken = jwtTokenProvider.generateRefreshToken();
		userRedisManager.saveRefreshToken(refreshToken, member.getId());

		CookieUtil.setCookie("accessToken", accessToken, jwtConfig.getAccessExpirySeconds(), response);
		CookieUtil.setCookie("refreshToken", refreshToken, jwtConfig.getRefreshExpirySeconds(), response);


		response.sendRedirect("https://honterview.site");
		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, body);
	}
}
