package com.i6.honterview.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.Question;
import com.i6.honterview.dto.request.InterviewCreateRequest;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.InterviewRepository;
import com.i6.honterview.repository.MemberRepository;
import com.i6.honterview.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

	private final InterviewRepository interviewRepository;
	private final MemberRepository memberRepository;
	private final QuestionRepository questionRepository;

	public Long createInterview(InterviewCreateRequest request, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Question question = questionRepository.findById(request.questionId())
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		Interview interview = interviewRepository.save(request.toEntity(member, question));
		return interview.getId();
	}

	public void deleteInterview(Long id, Long memberId) {
		Interview interview = interviewRepository.findWithQuestionsById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		if (!interview.isSameInterviewee(memberId)) {
			throw new CustomException(ErrorCode.INTERVIEWEE_NOT_SAME);
		}

		if (!interview.isDeletable()) {
			throw new CustomException(ErrorCode.INTERVIEW_DELETE_FORBIDDEN);
		}
		interviewRepository.delete(interview);
	}
}
