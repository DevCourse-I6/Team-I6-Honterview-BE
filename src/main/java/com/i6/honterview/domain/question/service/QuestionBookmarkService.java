package com.i6.honterview.domain.question.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.question.dto.response.QuestionBookmarkClickResponse;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.question.entity.QuestionBookmark;
import com.i6.honterview.domain.question.repository.QuestionBookmarkRepository;
import com.i6.honterview.domain.question.repository.QuestionRepository;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionBookmarkService {

	private final QuestionRepository questionRepository;
	private final MemberRepository memberRepository;
	private final QuestionBookmarkRepository questionBookmarkRepository;

	public QuestionBookmarkClickResponse clickQuestionBookmark(Long questionId, Long memberId) {
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Optional<QuestionBookmark> questionBookmarkOptional = questionBookmarkRepository.findByQuestionIdAndMemberId(
			question.getId(),
			member.getId());

		questionBookmarkOptional.ifPresentOrElse(
			questionBookmarkRepository::delete,
			() -> questionBookmarkRepository.save(new QuestionBookmark(question, member)));
		return new QuestionBookmarkClickResponse(questionBookmarkOptional.isEmpty());
	}

	public boolean isBookmarkedByMemberId(Long questionId, Long memberId) {
		return questionBookmarkRepository.existsByQuestionIdAndMemberId(questionId, memberId);
	}
}
