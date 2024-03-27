package com.i6.honterview.common.util;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	private static final String WEB_URL = ".honterview.site";

	private CookieUtil() {
	}

	public static void setCookie(String name, String value, int maxAge, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
			.domain(WEB_URL)
			.path("/")
			.maxAge(maxAge)
			.sameSite("None")
			.secure(true)
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public static void removeCookie(String name, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from(name, "")
			.maxAge(0)
			.path("/")
			.secure(true)
			.sameSite("None")
			.domain(WEB_URL)
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public static void setCookieLocal(String name, String value, int maxAge, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
			.domain("localhost:3000")
			.path("/")
			.maxAge(maxAge)
			.sameSite("None")
			.secure(true)
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}
}
