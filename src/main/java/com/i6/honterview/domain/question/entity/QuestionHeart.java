package com.i6.honterview.domain.question.entity;

import java.util.Objects;

import com.i6.honterview.common.entity.BaseEntity;
import com.i6.honterview.domain.user.entity.Member;

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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "question_heart")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class QuestionHeart extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Question question;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	public QuestionHeart(Question question, Member member) {
		this.question = question;
		this.member = member;
	}

	public boolean hasHeartedByMember(Long memberId) {
		return Objects.equals(this.member.getId(), memberId);
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
}
