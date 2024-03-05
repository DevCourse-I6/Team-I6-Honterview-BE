package com.i6.honterview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.security.auth.UserDetailsImpl;
import com.i6.honterview.service.FileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "파일")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@GetMapping("/upload-url")
	public ResponseEntity<String> getUploadUrl(
		@RequestParam Long interviewId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		String uploadUrl = fileService.generateUploadUrl(interviewId, userDetails.getId());
		return ResponseEntity.ok(uploadUrl);
	}
}
