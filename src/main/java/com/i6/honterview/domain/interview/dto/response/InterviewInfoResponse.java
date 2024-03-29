package com.i6.honterview.domain.interview.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.i6.honterview.domain.answer.entity.AnswerType;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.interview.entity.InterviewStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record InterviewInfoResponse(
	@Schema(description = "인터뷰 ID", example = "123")
	Long interviewId,

	@Schema(description = "타이머 시간(초), null일 경우 반환X", example = "90")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Integer timer,

	@Schema(description = "답변 타입(TEXT or RECORD)", example = "TEXT")
	AnswerType answerType,

	@Schema(description = "해당 면접의 총 질문 개수", example = "3")
	int questionCount,

	@Schema(description = "진행 상태", example = "IN_PROGRESS/COMPLETED")
	InterviewStatus status,

	@Schema(description = "영상 id, null일 경우 반환X", example = "123")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Long videoId,

	@Schema(description = "해당 면접의 질문&답변 목록")
	List<QuestionAndAnswerResponse> questionsAndAnswers,

	@Schema(description = "카테고리 이름 목록")
	List<String> categoryNames

) {
	public static InterviewInfoResponse of(Interview interview, List<QuestionAndAnswerResponse> questionsAndAnswers) {

		return new InterviewInfoResponse(
			interview.getId(),
			interview.getTimer(),
			interview.getAnswerType(),
			interview.getQuestionCount(),
			interview.getStatus(),
			interview.getVideo() != null ? interview.getVideo().getId() : null,
			questionsAndAnswers,
			interview.findFirstQuestion().getCategoryNames()
		);
	}
}
