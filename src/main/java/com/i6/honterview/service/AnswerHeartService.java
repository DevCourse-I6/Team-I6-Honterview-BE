package com.i6.honterview.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.AnswerHeart;
import com.i6.honterview.domain.Member;
import com.i6.honterview.dto.response.AnswerHeartClickResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.AnswerHeartRepository;
import com.i6.honterview.repository.AnswerRepository;
import com.i6.honterview.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerHeartService {

	private final AnswerHeartRepository answerHeartRepository;
	private final AnswerRepository answerRepository;
	private final MemberRepository memberRepository;

	public AnswerHeartClickResponse clickAnswerHeart(Long answerId, Long memberId) {
		Answer answer = answerRepository.findByIdWithHearts(answerId)
			.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

		Optional<AnswerHeart> answerHeartOptional = answer.findAnswerHeartByMemberId(memberId);
		answerHeartOptional.ifPresentOrElse(
			answerHeart -> {
				answer.removeHeart(answerHeart);
				answerHeartRepository.delete(answerHeart);
			},
			() -> addNewAnswerHeart(answer, memberId)
		);
		return new AnswerHeartClickResponse(answerHeartOptional.isEmpty());
	}

	private void addNewAnswerHeart(Answer answer, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		AnswerHeart answerHeart = answerHeartRepository.save(new AnswerHeart(answer, member));
		answer.addHeart(answerHeart);
	}
}
