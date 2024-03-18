package com.i6.honterview.domain.question.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.common.dto.ApiResponse;
import com.i6.honterview.common.dto.PageRequest;
import com.i6.honterview.common.dto.PageResponse;
import com.i6.honterview.common.security.resolver.CurrentAccount;
import com.i6.honterview.domain.question.dto.request.QuestionCreateRequest;
import com.i6.honterview.domain.question.dto.request.QuestionPageRequest;
import com.i6.honterview.domain.question.dto.response.QuestionBookmarkClickResponse;
import com.i6.honterview.domain.question.dto.response.QuestionDetailResponse;
import com.i6.honterview.domain.question.dto.response.QuestionHeartClickResponse;
import com.i6.honterview.domain.question.dto.response.QuestionResponse;
import com.i6.honterview.domain.question.dto.response.QuestionWithCategoriesResponse;
import com.i6.honterview.domain.question.service.QuestionBookmarkService;
import com.i6.honterview.domain.question.service.QuestionHeartService;
import com.i6.honterview.domain.question.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "질문")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {// TODO: 회원 연동

	private final QuestionService questionService;
	private final QuestionHeartService questionHeartService;
	private final QuestionBookmarkService questionBookmarkService;

	@Operation(summary = "질문 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<QuestionWithCategoriesResponse>>> getQuestions(
		@Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", defaultValue = "1") int page,
		@Parameter(description = "페이지 크기", example = "5") @RequestParam(value = "size", defaultValue = "5") int size,
		@Parameter(description = "검색어", example = "자바스크립트") @RequestParam(value = "query", required = false) String query,
		@Parameter(description = "조회할 카테고리 이름 목록", example = "프론트엔드") @RequestParam(value = "categories", required = false) List<String> categoryNames,
		@Parameter(description = "정렬 순서", example = "recent 최신순, hearts 좋아요순") @RequestParam(value = "order", defaultValue = "recent") String orderType
	) {
		QuestionPageRequest request = new QuestionPageRequest(page, size, query, categoryNames, orderType);
		PageResponse<QuestionWithCategoriesResponse> response = questionService.getQuestions(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "카테고리 이름에 의한 질문 전체 조회")
	@GetMapping("/by-category")
	public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByCategoryNames(
		@Parameter(description = "조회할 카테고리 이름 목록", example = "프론트엔드") @RequestParam(value = "categories") List<String> categoryNames
	) {
		List<QuestionResponse> response = questionService.getQuestionsByCategoryNames(categoryNames);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "질문 상세 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<QuestionDetailResponse>> getQuestionById(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id,
		@Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", defaultValue = "1") int page,
		@Parameter(description = "페이지 크기", example = "5") @RequestParam(value = "size", defaultValue = "5") int size
	) {
		PageRequest pageRequest = new PageRequest(page, size);
		QuestionDetailResponse response = questionService.getQuestionById(id, pageRequest);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "특정 질문에 대한 꼬리질문 3개 랜덤 조회")
	@GetMapping("/{id}/random")
	public ResponseEntity<ApiResponse<List<QuestionResponse>>> getRandomTailQuestions(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id
	) {
		List<QuestionResponse> response = questionService.getRandomTailQuestions(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "질문 생성")
	@PostMapping
	public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(
		@Valid @RequestBody QuestionCreateRequest request,
		@CurrentAccount Long memberId) {
		QuestionResponse question = questionService.createQuestion(request, memberId.toString());
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(question.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(question));
	}

	@Operation(summary = "질문 좋아요/좋아요 취소")
	@PostMapping("/{id}/hearts")
	public ResponseEntity<ApiResponse<QuestionHeartClickResponse>> clickQuestionHeart(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id,
		@CurrentAccount Long memberId) {
		QuestionHeartClickResponse response = questionHeartService.clickQuestionHeart(id, memberId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "질문 북마크/북마크 취소")
	@PostMapping("/{id}/bookmarks")
	public ResponseEntity<ApiResponse<QuestionBookmarkClickResponse>> clickQuestionBookmark(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id,
		@CurrentAccount Long memberId) {
		QuestionBookmarkClickResponse response = questionBookmarkService.clickQuestionBookmark(id, memberId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
