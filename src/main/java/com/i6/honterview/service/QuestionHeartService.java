package com.i6.honterview.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.QuestionHeart;
import com.i6.honterview.dto.response.QuestionHeartClickResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.MemberRepository;
import com.i6.honterview.repository.QuestionHeartRepository;
import com.i6.honterview.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionHeartService {

	private final QuestionHeartRepository questionHeartRepository;
	private final QuestionRepository questionRepository;
	private final MemberRepository memberRepository;

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
}
