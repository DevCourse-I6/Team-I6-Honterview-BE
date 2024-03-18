package com.i6.honterview.domain.user.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.common.dto.ApiResponse;
import com.i6.honterview.common.dto.PageResponse;
import com.i6.honterview.common.security.resolver.CurrentAccount;
import com.i6.honterview.domain.question.dto.request.CategoryCreateRequest;
import com.i6.honterview.domain.question.dto.request.CategoryUpdateRequest;
import com.i6.honterview.domain.question.dto.request.QuestionCreateRequest;
import com.i6.honterview.domain.question.dto.request.QuestionPageRequest;
import com.i6.honterview.domain.question.dto.request.QuestionUpdateRequest;
import com.i6.honterview.domain.question.dto.response.CategoryResponse;
import com.i6.honterview.domain.question.dto.response.QuestionResponse;
import com.i6.honterview.domain.question.dto.response.QuestionWithCategoriesResponse;
import com.i6.honterview.domain.question.service.CategoryService;
import com.i6.honterview.domain.question.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController { // TODO : 관리자용 질문 CRUD

	private final CategoryService categoryService;
	private final QuestionService questionService;

	@Operation(summary = "카테고리 생성")
	@PostMapping("/categories")
	public ResponseEntity<ApiResponse<Long>> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
		Long id = categoryService.createCategory(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(id));
	}

	@Operation(summary = "카테고리 수정")
	@PatchMapping("/categories/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
		@Parameter(description = "카테고리 id", example = "123") @PathVariable Long id,
		@Valid @RequestBody CategoryUpdateRequest request) {
		CategoryResponse response = categoryService.updateCategory(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "카테고리 삭제")
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<Void> deleteCategory(
		@Parameter(description = "카테고리 id", example = "123") @PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "카테고리 전체 조회(관리자)")
	@GetMapping("/categories")
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
		List<CategoryResponse> response = categoryService.getCategories();
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "질문 목록 조회(관리자)")
	@GetMapping("/questions")
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

	@Operation(summary = "질문 생성(관리자)")
	@PostMapping("/questions")
	public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(
		@Valid @RequestBody QuestionCreateRequest request,
		@CurrentAccount Long adminId) {
		QuestionResponse question = questionService.createQuestion(request, "ADMIN" + adminId);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(question.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(question));
	}

	@Operation(summary = "질문 수정")
	@PatchMapping("/questions/{id}")
	public ResponseEntity<Void> updateQuestion(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id,
		@Valid @RequestBody QuestionUpdateRequest request) {
		questionService.updateQuestion(id, request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "질문 삭제")
	@DeleteMapping("/questions/{id}")
	public ResponseEntity<Void> deleteQuestion(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id) {
		questionService.deleteQuestion(id);
		return ResponseEntity.noContent().build();
	}
}
