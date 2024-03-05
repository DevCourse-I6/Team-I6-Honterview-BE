package com.i6.honterview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.response.DownloadUrlResponse;
import com.i6.honterview.dto.response.UploadUrlResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.security.auth.UserDetailsImpl;
import com.i6.honterview.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "파일")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@Operation(summary = "파일 업로드 URL 조회")
	@GetMapping("/upload-url")
	public ResponseEntity<ApiResponse<UploadUrlResponse>> getUploadUrl(
		@Parameter(description = "인터뷰 id", example = "123") @RequestParam Long interviewId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		UploadUrlResponse response = fileService.generateUploadUrl(interviewId, userDetails.getId());
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "파일 다운로드 URL 조회")
	@GetMapping("/download-url/{videoId}")
	public ResponseEntity<ApiResponse<DownloadUrlResponse>> getDownloadUrl(
		@Parameter(description = "영상 id", example = "123") @PathVariable Long videoId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		DownloadUrlResponse response = fileService.generateDownloadUrl(videoId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
