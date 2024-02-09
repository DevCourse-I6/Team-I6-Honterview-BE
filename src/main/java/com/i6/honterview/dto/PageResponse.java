package com.i6.honterview.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
	int currentPage,
	int pageSize,
	long totalElements,
	int startIndex,
	int endIndex,
	boolean hasPreviousPage,
	boolean hasNextPage,
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
