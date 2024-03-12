package com.i6.honterview.domain.interview.dto.response;

import com.i6.honterview.domain.interview.entity.Video;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "업로드 URL 응답")
public record UploadUrlResponse(
	@Schema(description = "영상 id", example = "123")
	Long videoId,

	@Schema(description = "업로드 URL", example = "https://honterview-s3-bucket.com")
	String uploadUrl
) {
	public static UploadUrlResponse of(Video video, String uploadUrl) {
		return new UploadUrlResponse(
			video.getId(),
			uploadUrl
		);
	}
}
