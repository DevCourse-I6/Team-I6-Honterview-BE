package com.i6.honterview.common.security.jwt;

import static com.i6.honterview.common.exception.SecurityErrorCode.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.i6.honterview.common.util.HttpResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.warn("Access Denied: ", accessDeniedException);
		HttpResponseUtil.writeErrorResponse(response, HttpStatus.FORBIDDEN, FORBIDDEN.getErrorResponse());
	}
}
