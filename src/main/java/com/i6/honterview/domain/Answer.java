package com.i6.honterview.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.i6.honterview.dto.response.AnswerHeartClickResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "answer")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Question question;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@OneToMany(mappedBy = "answer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<AnswerHeart> answerHearts = new HashSet<>();

	public void addHeart(AnswerHeart heart) {
		answerHearts.add(heart);
		heart.setAnswer(this);
	}

	public void removeHeart(AnswerHeart heart) {
		answerHearts.remove(heart);
	}

	public Optional<AnswerHeart> findAnswerHeartByMemberId(Long memberId) {
		return answerHearts.stream()
			.filter(heart -> heart.hasHeartedByMember(memberId))
			.findFirst();
	}
}
