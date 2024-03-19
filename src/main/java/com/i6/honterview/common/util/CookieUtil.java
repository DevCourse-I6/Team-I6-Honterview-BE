package com.i6.honterview.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	private static final String WEB_URL = "*.honterview.site";

	private CookieUtil() {
	}

	public static void setCookie(String name, String value, int maxAge, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setSecure(true);
		cookie.setDomain(WEB_URL);
		response.addCookie(cookie);
	}

	public static void removeCookie(String name, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setDomain(WEB_URL);
		response.addCookie(cookie);
	}
}
