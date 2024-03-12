package com.i6.honterview.domain.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "마이페이지")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MypageController {
}
