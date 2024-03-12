package com.i6.honterview.domain.question.entity;

import java.util.HashSet;
import java.util.Set;

import com.i6.honterview.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Category extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<QuestionCategory> questionCategories = new HashSet<>();

	public Category(String categoryName) {
		this.categoryName = categoryName;
	}

	public void changeCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
