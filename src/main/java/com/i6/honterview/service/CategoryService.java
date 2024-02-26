package com.i6.honterview.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Category;
import com.i6.honterview.dto.request.CategoryCreateRequest;
import com.i6.honterview.dto.request.CategoryUpdateRequest;
import com.i6.honterview.dto.response.CategoryResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

	private static final int CATEGORY_MAX_COUNT = 3;
	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryResponse> getCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(CategoryResponse::from).toList();
	}

	public Long createCategory(CategoryCreateRequest request) {
		categoryRepository.findByCategoryName(request.categoryName())
			.ifPresent(c -> {
				throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATED);
			});

		Category category = categoryRepository.save(request.toEntity());
		return category.getId();
	}

	public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

		categoryRepository.findByCategoryName(request.categoryName())
			.filter(c -> !c.getId().equals(id))
			.ifPresent(c -> {
				throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATED);
			});

		category.changeCategoryName(request.categoryName());
		category = categoryRepository.save(category);
		return CategoryResponse.from(category);
	}

	public void deleteCategory(Long id) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
		categoryRepository.delete(category);
	}

	public List<Category> validateAndGetCategories(List<Long> categoryIds) {
		int categoryIdsSize = categoryIds.size();
		if (categoryIdsSize == 0 || categoryIdsSize > CATEGORY_MAX_COUNT)
			throw new CustomException(ErrorCode.INVALID_CATEGORY_COUNT);

		List<Category> categories = categoryRepository.findAllById(categoryIds);
		// Question은 적어도 하나 이상의 카테고리와 연결되어 있어야 함
		if (categories.isEmpty())
			throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);

		return categories;
	}

}
