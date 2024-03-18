package com.i6.honterview.common.dto;

import org.springframework.data.domain.Pageable;

public class PageRequest {

	private int page;
	private int size;

	public PageRequest(Integer page, Integer size) {
		this.page = page > 0 ? page : 1;
		this.size = size > 0 ? size : 5;
	}

	public Pageable getPageable() {
		return org.springframework.data.domain.PageRequest.of(page - 1, size);
	}
}
