package com.i6.honterview.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.i6.honterview.security.service.Oauth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final Oauth2UserService oauth2UserService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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

			// DOCS
			antMatcher("/swagger-ui/**"),
			antMatcher("/swagger-ui"),
			antMatcher("/swagger-ui.html"),
			antMatcher("/swagger/**"),
			antMatcher("/swagger-resources/**"),
			antMatcher("/v3/api-docs/**"),
			antMatcher("/webjars/**"),

			// H2-CONSOLE
			antMatcher("/h2-console/**")
		);
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	/**
	 * OAuth 관련 엔드포인트에 적용되는 SecurityFilterChain 입니다.
	 */
	@Bean
	public SecurityFilterChain securityFilterChainOAuth(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.securityMatchers(matchers -> matchers
				.requestMatchers(
					antMatcher("/login"),
					antMatcher("/login/oauth2/code/kakao"),
					antMatcher("/oauth2/authorization/kakao")
				))
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest()
				.permitAll())
			.oauth2Login(
				(oauth) ->
					oauth.userInfoEndpoint(
							(endpoint) -> endpoint.userService(oauth2UserService)
						)
			//			.successHandler(oauthSuccessHandler)
			);
		return http.build();
	}


	private void configureCommonSecuritySettings(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.anonymous(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.headers(headers -> headers
				.frameOptions(frameOptions -> frameOptions.disable()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}
}
