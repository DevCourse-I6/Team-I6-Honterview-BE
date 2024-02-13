package com.i6.honterview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
