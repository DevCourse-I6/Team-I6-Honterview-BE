package com.i6.honterview.domain.question.dto.request;

import java.util.List;

import com.i6.honterview.common.dto.PageRequest;

import lombok.Getter;

@Getter
public class QuestionPageRequest extends PageRequest {
	private String query;
	private List<String> categoryNames;
	private String orderType;

	public QuestionPageRequest(int page, int size, String query, List<String> categoryNames, String orderType) {
		super(page, size);
		this.query = query;
		this.categoryNames = categoryNames;
		this.orderType = orderType;
	}
}
