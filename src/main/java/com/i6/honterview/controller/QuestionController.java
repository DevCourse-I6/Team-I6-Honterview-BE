package com.i6.honterview.controller;

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

import com.i6.honterview.dto.request.QuestionUpdateRequest;
import com.i6.honterview.dto.response.PageResponse;
import com.i6.honterview.dto.response.QuestionDetailResponse;
import com.i6.honterview.dto.response.QuestionHeartClickResponse;
import com.i6.honterview.dto.response.QuestionResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.QuestionHeartService;
import com.i6.honterview.service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "질문")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionHeartService questionHeartService;

	@Operation(summary = "질문 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<QuestionResponse>>> getQuestions(
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "5") int size,
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "categories", required = false) List<String> categoryNames,
		@RequestParam(value = "order", defaultValue = "recent") String orderType
	) {
		PageResponse<QuestionResponse> response =
			questionService.getQuestions(page, size, query, categoryNames, orderType);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "질문 상세 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<QuestionDetailResponse>> getQuestionById(
		@PathVariable Long id,
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "5") int size
	) {
		QuestionDetailResponse response = questionService.getQuestionById(id, page, size);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "질문 수정")
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateQuestion(
		@PathVariable Long id, @Valid @RequestBody QuestionUpdateRequest request) {
		questionService.updateQuestion(id, request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "질문 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
		questionService.deleteQuestion(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "질문 좋아요/좋아요 취소")
	@PostMapping("/{id}/hearts")
	public ResponseEntity<ApiResponse<QuestionHeartClickResponse>> clickQuestionHeart(@PathVariable Long id) {
		QuestionHeartClickResponse response = questionHeartService.clickQuestionHeart(id, 1L); //TODO: 회원 연동
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
