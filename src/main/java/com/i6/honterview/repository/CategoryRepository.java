package com.i6.honterview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryName(String categoryName);
}
