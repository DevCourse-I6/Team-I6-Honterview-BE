package com.i6.honterview.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "question")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Question extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "hearts_count", nullable = false)
	private Long heartsCount = 0L;

	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<QuestionCategory> questionCategories = new HashSet<>();

	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<QuestionHeart> questionHearts = new HashSet<>();

	@Column(name = "created_by", nullable = false)
	private String createdBy;

	@Builder
	public Question(String content, Long parentId, List<Category> categories, String createdBy) {
		this.content = content;
		this.parentId = parentId;
		this.createdBy = createdBy;
		setQuestionCategories(categories);
	}

	public void changeContentAndCategories(String content, List<Category> categories) {
		this.content = content;
		this.questionCategories.clear();
		setQuestionCategories(categories);
	}

	public Optional<QuestionHeart> findQuestionHeartByMemberId(Long memberId) {
		return questionHearts.stream()
			.filter(heart -> heart.hasHeartedByMember(memberId))
			.findFirst();
	}

	private void setQuestionCategories(List<Category> categories) {
		this.questionCategories.clear();
		this.questionCategories.addAll(categories.stream()
			.map(category -> new QuestionCategory(this, category))
			.toList());
	}

	public void addHeart(QuestionHeart heart) {
		questionHearts.add(heart);
		heart.setQuestion(this);
		this.heartsCount++;
	}

	public void removeHeart(QuestionHeart heart) {
		questionHearts.remove(heart);
		this.heartsCount--;
	}

	public List<Long> getCategoryIds() {
		return this.questionCategories.stream()
			.map(questionCategory -> questionCategory.getCategory().getId())
			.toList();
	}

	public List<String> getCategoryNames() {
		return this.questionCategories.stream()
			.map(category -> category.getCategory().getCategoryName())
			.toList();
	}
}
