package com.i6.honterview.domain.interview.controller;

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

import com.i6.honterview.common.dto.ApiResponse;
import com.i6.honterview.common.security.resolver.CurrentAccount;
import com.i6.honterview.domain.answer.dto.request.AnswerVisibilityUpdateRequest;
import com.i6.honterview.domain.answer.dto.response.AnswersVisibilityUpdateResponse;
import com.i6.honterview.domain.gpt.dto.request.GptNewQuestionCreateRequest;
import com.i6.honterview.domain.gpt.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.domain.gpt.dto.response.GptQuestionCreateResponse;
import com.i6.honterview.domain.interview.dto.request.InterviewCreateRequest;
import com.i6.honterview.domain.interview.dto.request.QuestionAnswerCreateRequest;
import com.i6.honterview.domain.interview.dto.response.InterviewInfoResponse;
import com.i6.honterview.domain.interview.dto.response.QuestionAnswerCreateResponse;
import com.i6.honterview.domain.interview.service.InterviewService;

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
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id,
		@CurrentAccount Long memberId
	) {
		interviewService.updateInterviewStatus(id, memberId);
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
	public ResponseEntity<ApiResponse<QuestionAnswerCreateResponse>> createQuestionAndAnswer(
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id,
		@RequestBody QuestionAnswerCreateRequest request,
		@CurrentAccount Long memberId
	) {
		QuestionAnswerCreateResponse response = interviewService.createQuestionAndAnswer(id, memberId, request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "답변 공개여부 수정(면접 결과)")
	@PatchMapping("/{id}/visibility")
	public ResponseEntity<ApiResponse<AnswersVisibilityUpdateResponse>> changeAnswersVisibility(
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id,
		@RequestBody List<AnswerVisibilityUpdateRequest> request,
		@CurrentAccount Long memberId
	) {
		AnswersVisibilityUpdateResponse response = interviewService.changeAnswersVisibility(id, request, memberId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "면접 현황 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InterviewInfoResponse>> getInterviewInfo(
		@Parameter(description = "면접 id", example = "123") @PathVariable Long id,
		@CurrentAccount Long memberId
	) {
		InterviewInfoResponse response = interviewService.getInterviewInfo(id, memberId);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "GPT 꼬리질문 생성(다음 질문 넘어갈 때 호출)")
	@PostMapping("/gpt/{interviewId}")
	public ResponseEntity<ApiResponse<GptQuestionCreateResponse>> createTailGptQuestion(
		@PathVariable Long interviewId,
		@Valid @RequestBody GptQuestionCreateRequest request
	) {
		GptQuestionCreateResponse response = interviewService.generateTailQuestion(interviewId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}

	@Operation(summary = "GPT 꼬리질문 재생성")
	@PostMapping("/gpt/{interviewId}/new")
	public ResponseEntity<ApiResponse<GptQuestionCreateResponse>> createNewGptQuestion(
		@PathVariable Long interviewId,
		@Valid @RequestBody GptNewQuestionCreateRequest request
	) {
		GptQuestionCreateResponse response = interviewService.regenerateTailQuestion(interviewId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}
}
