package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Record;

public record FileUploadResponse(
	Long recordId,
	String fileName
) {
	public static FileUploadResponse from(Record record) {
		return new FileUploadResponse(
			record.getId(),
			record.getFileName()
		);
	}
}
