package com.i6.honterview.domain.question.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.question.entity.QuestionBookmark;
import com.i6.honterview.domain.question.entity.QuestionHeart;
import com.i6.honterview.domain.question.dto.response.QuestionBookmarkClickResponse;
import com.i6.honterview.domain.question.dto.response.QuestionHeartClickResponse;
import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;
import com.i6.honterview.domain.user.repository.MemberRepository;
import com.i6.honterview.domain.question.repository.QuestionBookmarkRepository;
import com.i6.honterview.domain.question.repository.QuestionHeartRepository;
import com.i6.honterview.domain.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionHeartService {

	private final QuestionRepository questionRepository;
	private final MemberRepository memberRepository;
	private final QuestionHeartRepository questionHeartRepository;
	private final QuestionBookmarkRepository questionBookmarkRepository;

	public QuestionHeartClickResponse clickQuestionHeart(Long questionId, Long memberId) {
		Question question = questionRepository.findByIdWithHearts(questionId)
			.orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

		Optional<QuestionHeart> questionHeartOptional = question.findQuestionHeartByMemberId(memberId);
		questionHeartOptional.ifPresentOrElse(
			questionHeart -> {
				question.removeHeart(questionHeart);
				questionHeartRepository.delete(questionHeart);
			},
			() -> addNewQuestionHeart(question, memberId)
		);
		return new QuestionHeartClickResponse(question.getHeartsCount(), questionHeartOptional.isEmpty());
	}

	private void addNewQuestionHeart(Question question, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		QuestionHeart questionHeart = questionHeartRepository.save(new QuestionHeart(question, member));
		question.addHeart(questionHeart);
	}

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
}