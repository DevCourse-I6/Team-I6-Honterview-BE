package com.i6.honterview.dto.response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.QuestionCategory;

import io.swagger.v3.oas.annotations.media.Schema;

public record InterviewInfoResponse(
	@Schema(description = "인터뷰 ID", example = "123")
	Long interviewId,

	@Schema(description = "타이머 시간(초)", example = "90")
	Integer timer,

	@Schema(description = "해당 면접의 총 질문 개수", example = "3")
	int questionCount,

	@Schema(description = "해당 면접의 답변이 완료된 질문 목록")
	List<QuestionInterviewingResponse> questions,

	@Schema(description = "카테고리 이름 목록")
	List<String> categoryNames

) {
	public static InterviewInfoResponse of(Interview interview, Set<QuestionCategory> categories) {
		List<QuestionInterviewingResponse> questions = interview.getInterviewQuestions().stream()
			.map(interviewQuestion -> QuestionInterviewingResponse.from(
				interviewQuestion.getQuestion(),
				interviewQuestion.getProcessingTime()
			))
			.collect(Collectors.toList());

		List<String> categoryNames = categories.stream()
			.map(category -> category.getCategory().getCategoryName())
			.toList();

		return new InterviewInfoResponse(
			interview.getId(),
			interview.getTimer(),
			interview.getQuestionCount(),
			questions,
			categoryNames
		);
	}
}
