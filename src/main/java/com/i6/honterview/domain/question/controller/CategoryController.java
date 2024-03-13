package com.i6.honterview.domain.question.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.common.dto.ApiResponse;
import com.i6.honterview.domain.question.dto.response.CategoryResponse;
import com.i6.honterview.domain.question.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private final CategoryService categoryService;

	@Operation(summary = "카테고리 전체 조회(모든 이용자 접근 가능)")
	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
		List<CategoryResponse> response = categoryService.getCategories();
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
