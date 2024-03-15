package com.i6.honterview.domain.interview.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.i6.honterview.common.entity.BaseEntity;
import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.answer.entity.AnswerType;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.user.entity.Member;

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
import jakarta.persistence.OneToOne;
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
	private int questionCount;

	@Column(name = "interview_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private InterviewStatus status = InterviewStatus.IN_PROGRESS;

	@Column(name = "timer")
	private Integer timer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InterviewQuestion> interviewQuestions = new ArrayList<>();

	@OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Answer> answers = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Video video;

	public Interview(AnswerType answerType, Integer questionCount, Integer timer, Member member, Question question) {
		this.answerType = answerType;
		this.questionCount = questionCount;
		this.timer = timer;
		this.member = member;
		this.interviewQuestions.add(new InterviewQuestion(this, question));
	}

	public void changeStatus(InterviewStatus status) {
		this.status = status;
	}

	public boolean isSameInterviewee(Long id) {
		return Objects.equals(this.member.getId(), id);
	}

	public boolean isDeletable() {
		return this.status != InterviewStatus.COMPLETED;
	}

	public void addQuestion(Question question) {
		this.interviewQuestions.add(new InterviewQuestion(this, question));
	}

	public Question findFirstQuestion() {
		if (interviewQuestions != null && !interviewQuestions.isEmpty()) {
			return interviewQuestions.get(0).getQuestion();
		} else {
			throw new CustomException(ErrorCode.FIRST_QUESTION_NOT_FOUND);
		}
	}
}
