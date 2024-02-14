package com.i6.honterview.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.response.PageResponse;
import com.i6.honterview.dto.response.QuestionDetailResponse;
import com.i6.honterview.dto.response.QuestionResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

	private final QuestionService questionService;

	@GetMapping
	public ResponseEntity<ApiResponse> getQuestions(
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

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getQuestionById(@PathVariable Long id) {
		QuestionDetailResponse response = questionService.getQuestionById(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

}
