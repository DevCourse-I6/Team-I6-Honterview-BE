package com.i6.honterview.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.i6.honterview.security.jwt.JwtAccessDeniedHandler;
import com.i6.honterview.security.jwt.JwtAuthenticationEntryPoint;
import com.i6.honterview.security.jwt.JwtAuthenticationFilter;
import com.i6.honterview.security.jwt.JwtTokenProvider;
import com.i6.honterview.security.service.OAuth2AuthenticationSuccessHandler;
import com.i6.honterview.security.service.Oauth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final Oauth2UserService oauth2UserService;
	private final OAuth2AuthenticationSuccessHandler oauthSuccessHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * permitAll 권한을 가진 엔드포인트에 적용되는 SecurityFilterChain
	 */
	@Bean
	public SecurityFilterChain securityFilterChainPermitAll(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http.securityMatchers(matchers -> matchers.requestMatchers(requestPermitAll()))
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.permitAll());
		return http.build();
	}

	private RequestMatcher[] requestPermitAll() {
		List<RequestMatcher> requestMatchers = List.of(
			// Member
			antMatcher("/api/*/auth/reissue"),

			// DOCS
			antMatcher("/swagger-ui/**"),
			antMatcher("/swagger-ui"),
			antMatcher("/swagger-ui.html"),
			antMatcher("/swagger/**"),
			antMatcher("/swagger-resources/**"),
			antMatcher("/v3/api-docs/**"),
			antMatcher("/webjars/**"),
			antMatcher("/favicon.ico"),

			// H2-CONSOLE
			antMatcher("/h2-console/**")
		);
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	/**
	 * OAuth 관련 엔드포인트에 적용되는 SecurityFilterChain
	 */
	@Bean
	public SecurityFilterChain securityFilterChainOAuth(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.securityMatchers(matchers -> matchers
				.requestMatchers(
					antMatcher("/login"),
					antMatcher("/login/oauth2/code/**"),
					antMatcher("/oauth2/authorization/**")
				))
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.permitAll())
			.oauth2Login(
				(oauth) ->
					oauth.userInfoEndpoint(
							(endpoint) -> endpoint.userService(oauth2UserService)
						)
						.successHandler(oauthSuccessHandler)
			);
		return http.build();
	}

	/**
	 * 위에서 정의된 엔드포인트 이외에는 authenticated 로 설정
	 */
	@Bean
	public SecurityFilterChain securityFilterChainDefault(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.permitAll()	// TODO : Authenticated()로 변경
			)
			.addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider), ExceptionTranslationFilter.class)
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				exception.accessDeniedHandler(jwtAccessDeniedHandler);
			});
		return http.build();
	}

	private void configureCommonSecuritySettings(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.anonymous(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}
}
