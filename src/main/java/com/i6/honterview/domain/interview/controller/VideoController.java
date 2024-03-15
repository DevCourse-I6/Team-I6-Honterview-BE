package com.i6.honterview.domain.interview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.common.dto.ApiResponse;
import com.i6.honterview.common.security.resolver.CurrentAccount;
import com.i6.honterview.domain.interview.dto.response.DownloadUrlResponse;
import com.i6.honterview.domain.interview.dto.response.UploadUrlResponse;
import com.i6.honterview.domain.interview.service.VideoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "영상")
@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {

	private final VideoService videoService;

	@Operation(summary = "영상 업로드 URL 조회")
	@GetMapping("/upload-url")
	public ResponseEntity<ApiResponse<UploadUrlResponse>> getUploadUrl(
		@Parameter(description = "인터뷰 id", example = "123") @RequestParam Long interviewId,
		@CurrentAccount Long memberId
	) {
		UploadUrlResponse response = videoService.generateUploadUrl(interviewId, memberId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "영상 다운로드 URL 조회")
	@GetMapping("/download-url/{videoId}")
	public ResponseEntity<ApiResponse<DownloadUrlResponse>> getDownloadUrl(
		@Parameter(description = "영상 id", example = "123") @PathVariable Long videoId,
		@CurrentAccount Long memberId // TODO:?? 다운로드시 member 비교 로직을 위한걸까요?
	) {
		DownloadUrlResponse response = videoService.generateDownloadUrl(videoId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
