package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파일 업로드 응답")
public record FileUploadResponse(
	@Schema(description = "녹음 id", example = "123")
	Long recordId,

	@Schema(description = "파일 이름", example = "honterview_202403040929137878.mp3")
	String fileName
) {
	public static FileUploadResponse from(Record record) {
		return new FileUploadResponse(
			record.getId(),
			record.getFileName()
		);
	}
}
