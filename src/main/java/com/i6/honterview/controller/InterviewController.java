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

import com.i6.honterview.dto.request.AnswerVisibilityUpdateRequest;
import com.i6.honterview.dto.request.InterviewCreateRequest;
import com.i6.honterview.dto.request.QuestionAnswerCreateRequest;
import com.i6.honterview.dto.response.AnswersVisibilityUpdateResponse;
import com.i6.honterview.dto.response.InterviewInfoResponse;
import com.i6.honterview.dto.response.QuestionAnswerCreateResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.security.resolver.CurrentAccount;
import com.i6.honterview.service.InterviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "면접 연습")
@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {

	private final InterviewService interviewService;

	@Operation(summary = "면접 연습 생성(면접 시작 전)")
	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createInterview(
		@Valid @RequestBody InterviewCreateRequest request,
		@CurrentAccount Long memberId) {
		Long id = interviewService.createInterview(request, memberId);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(id));
	}

	@Operation(summary = "면접 상태 수정(면접 완료)")
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateInterviewStatus(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id) {
		interviewService.updateInterviewStatus(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "면접 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInterview(
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id,
		@CurrentAccount Long memberId) {
		interviewService.deleteInterview(id, memberId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "면접/답변 저장(면접 연습 중)")
	@PostMapping("/{id}")
	public ResponseEntity<ApiResponse<QuestionAnswerCreateResponse>> createQuestionAndAnswer(// TODO: 멤버 연동
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id,
		@RequestBody QuestionAnswerCreateRequest request
	) {
		QuestionAnswerCreateResponse response = interviewService.createQuestionAndAnswer(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "답변 공개여부 수정(면접 결과)")
	@PatchMapping("/{id}/visibility")
	public ResponseEntity<ApiResponse<AnswersVisibilityUpdateResponse>> changeAnswersVisibility(// TODO: 멤버 연동
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id,
		@RequestBody List<AnswerVisibilityUpdateRequest> request
	) {
		AnswersVisibilityUpdateResponse response = interviewService.changeAnswersVisibility(id, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "면접 현황 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InterviewInfoResponse>> getInterviewInfo(
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id
	) {
		InterviewInfoResponse response = interviewService.getInterviewInfo(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
