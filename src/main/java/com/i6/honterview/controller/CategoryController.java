package com.i6.honterview.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.dto.request.CategoryCreateRequest;
import com.i6.honterview.dto.request.CategoryUpdateRequest;
import com.i6.honterview.dto.response.CategoryResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private final CategoryService categoryService;

	@Operation(summary = "카테고리 전체 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
		List<CategoryResponse> response = categoryService.getCategories();
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "카테고리 생성")
	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
		Long id = categoryService.createCategory(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(id));
	}

	@Operation(summary = "카테고리 수정")
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
		@PathVariable Long id,
		@Valid @RequestBody CategoryUpdateRequest request) {
		CategoryResponse response = categoryService.updateCategory(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "카테고리 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

}
