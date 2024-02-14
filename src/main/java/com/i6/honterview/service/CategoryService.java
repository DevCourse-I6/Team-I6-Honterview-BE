package com.i6.honterview.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.i6.honterview.domain.Category;
import com.i6.honterview.dto.response.CategoryResponse;
import com.i6.honterview.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public List<CategoryResponse> getCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(CategoryResponse::from).toList();
	}
}
