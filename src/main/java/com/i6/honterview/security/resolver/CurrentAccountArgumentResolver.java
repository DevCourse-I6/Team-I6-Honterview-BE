package com.i6.honterview.security.resolver;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.i6.honterview.exception.SecurityCustomException;
import com.i6.honterview.exception.SecurityErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentAccountArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasParameterAnnotation = parameter.hasParameterAnnotation(CurrentAccount.class);
		boolean hasLongParameterType = parameter.getParameterType().isAssignableFrom(Long.class);
		return hasParameterAnnotation && hasLongParameterType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//validateAuthentication(authentication);
		//UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal(); // TODO: 추후 주석 제거
		return 1L; // TODO: userDetails.getId()로 변경
	}

	private void validateAuthentication(Authentication authentication) {
		if (Objects.isNull(authentication) || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new SecurityCustomException(SecurityErrorCode.UNAUTHORIZED);
		}
	}
}
