package com.i6.honterview.domain.question.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.question.dto.response.QuestionHeartClickResponse;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.question.entity.QuestionHeart;
import com.i6.honterview.domain.question.repository.QuestionHeartRepository;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionHeartService {

	private final QuestionHeartRepository questionHeartRepository;
	private final QuestionService questionService;
	private final MemberService memberService;

	public QuestionHeartClickResponse clickQuestionHeart(Long questionId, Long memberId) {
		Question question = questionService.findByIdWithHearts(questionId);

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
		Member member = memberService.findById(memberId);
		QuestionHeart questionHeart = questionHeartRepository.save(new QuestionHeart(question, member));
		question.addHeart(questionHeart);
	}

}
