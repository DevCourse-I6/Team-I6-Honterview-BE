package com.i6.honterview.domain.question.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.question.dto.response.QuestionBookmarkClickResponse;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.question.entity.QuestionBookmark;
import com.i6.honterview.domain.question.repository.QuestionBookmarkRepository;
import com.i6.honterview.domain.user.entity.Member;
import com.i6.honterview.domain.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionBookmarkService {

	private final QuestionService questionService;
	private final MemberService memberService;
	private final QuestionBookmarkRepository questionBookmarkRepository;

	public QuestionBookmarkClickResponse clickQuestionBookmark(Long questionId, Long memberId) {
		Question question = questionService.findById(questionId);
		Member member = memberService.findById(memberId);

		Optional<QuestionBookmark> questionBookmarkOptional = questionBookmarkRepository.findByQuestionIdAndMemberId(
			question.getId(),
			member.getId());

		questionBookmarkOptional.ifPresentOrElse(
			questionBookmarkRepository::delete,
			() -> questionBookmarkRepository.save(new QuestionBookmark(question, member)));
		return new QuestionBookmarkClickResponse(questionBookmarkOptional.isEmpty());
	}
}
