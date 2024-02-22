package com.i6.honterview.domain;

import java.util.ArrayList;
import java.util.List;

import com.i6.honterview.domain.enums.AnswerType;
import com.i6.honterview.domain.enums.InterviewStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "interview")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Interview extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "answer_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AnswerType answerType;

	@Column(name = "question_count", nullable = false)
	private Integer questionCount;

	@Column(name = "interview_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private InterviewStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InterviewQuestion> interviewQuestions = new ArrayList<>();

	public Interview(AnswerType answerType, Integer questionCount, InterviewStatus status, Member member,
		Question question) {
		this.answerType = answerType;
		this.questionCount = questionCount;
		this.status = status;
		this.member = member;
		this.interviewQuestions.add(new InterviewQuestion(this, question));
	}

	public void changeStatus(InterviewStatus status) {
		this.status = status;
	}
}
