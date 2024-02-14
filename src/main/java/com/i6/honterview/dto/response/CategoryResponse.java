package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Category;

public record CategoryResponse(String name) {
	public static CategoryResponse from(Category category) {
		return new CategoryResponse(category.getCategoryName());
	}
}
