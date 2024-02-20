package com.i6.honterview.dto.response;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "페이지 응답")
public record PageResponse<T>(
	@Schema(description = "현재 페이지", example = "1")
	int currentPage,
	@Schema(description = "페이지 크기", example = "5")
	int pageSize,
	@Schema(description = "전체 항목 수", example = "100")
	long totalElements,
	@Schema(description = "시작 인덱스", example = "1")
	int startIndex,
	@Schema(description = "종료 인덱스", example = "5")
	int endIndex,
	@Schema(description = "이전 페이지 존재여부", example = "false")
	boolean hasPreviousPage,
	@Schema(description = "다음 페이지 존재여부", example = "true")
	boolean hasNextPage,
	@Schema(description = "데이터 목록")
	List<T> data
) {
	public static <T, R> PageResponse<R> of(Page<T> page, Function<T, R> converter) {
		int currentPage = page.getNumber() + 1;
		int pageSize = page.getSize();
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();

		int startIndex = (currentPage - 1) * pageSize + 1;
		int endIndex = Math.min(currentPage * pageSize, (int)totalElements);

		boolean hasPreviousPage = currentPage > 1;
		boolean hasNextPage = currentPage < totalPages;

		List<R> content = page.getContent().stream().map(converter).toList();

		return new PageResponse<>(
			currentPage,
			pageSize,
			totalElements,
			startIndex,
			endIndex,
			hasPreviousPage,
			hasNextPage,
			content
		);
	}
}
