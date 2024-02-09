package com.i6.honterview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthController {// TODO : 프론트엔드 연동 후 제거

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
