package com.i6.honterview.domain.answer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.answer.dto.response.AnswerHeartClickResponse;
import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.answer.entity.AnswerHeart;
import com.i6.honterview.domain.answer.repository.AnswerHeartRepository;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerHeartService {

	private final AnswerHeartRepository answerHeartRepository;
	private final AnswerService answerService;
	private final MemberService memberService;

	public AnswerHeartClickResponse clickAnswerHeart(Long answerId, Long memberId) {
		Answer answer = answerService.findByIdWithHearts(answerId);

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
		Member member = memberService.findById(memberId);
		AnswerHeart answerHeart = answerHeartRepository.save(new AnswerHeart(answer, member));
		answer.addHeart(answerHeart);
	}
}
