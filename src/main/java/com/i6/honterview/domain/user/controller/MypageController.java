package com.i6.honterview.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.common.dto.ApiResponse;
import com.i6.honterview.common.dto.PageResponse;
import com.i6.honterview.common.security.resolver.CurrentAccount;
import com.i6.honterview.domain.interview.service.InterviewService;
import com.i6.honterview.domain.question.dto.response.QuestionWithCategoriesResponse;
import com.i6.honterview.domain.question.service.QuestionService;
import com.i6.honterview.domain.user.dto.request.MemberUpdateRequest;
import com.i6.honterview.domain.user.dto.response.InterviewMypageResponse;
import com.i6.honterview.domain.user.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "마이페이지")
@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final InterviewService interviewService;
	private final QuestionService questionService;
	private final MemberService memberService;

	@Operation(summary = "마이페이지 면접 목록 조회")
	@GetMapping("/interviews")
	public ResponseEntity<ApiResponse<PageResponse<InterviewMypageResponse>>> getInterviewsMypage(
		@Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", defaultValue = "1") int page,
		@Parameter(description = "페이지 크기", example = "5") @RequestParam(value = "size", defaultValue = "5") int size,
		@CurrentAccount Long memberId
	) {
		PageResponse<InterviewMypageResponse> response = interviewService.getInterviewsMypage(page, size, memberId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "마이페이지 북마크 질문 목록 조회")
	@GetMapping("/bookmarks")
	public ResponseEntity<ApiResponse<PageResponse<QuestionWithCategoriesResponse>>> getBookmarkedQuestionsMypage(
		@Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", defaultValue = "1") int page,
		@Parameter(description = "페이지 크기", example = "5") @RequestParam(value = "size", defaultValue = "5") int size,
		@CurrentAccount Long memberId
	) {
		PageResponse<QuestionWithCategoriesResponse> response =
			questionService.getBookmarkedQuestionsMypage(page, size, memberId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "닉네임 수정")
	@PatchMapping("/nickname")
	public ResponseEntity<ApiResponse<String>> updateNickname(
		@CurrentAccount Long memberId,
		@Valid @RequestBody MemberUpdateRequest request) {
		String response = memberService.updateNickname(memberId, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

}
