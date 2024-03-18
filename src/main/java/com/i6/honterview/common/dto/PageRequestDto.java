package com.i6.honterview.common.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public abstract class PageRequestDto {

	protected int page;
	protected int size;
	
	protected PageRequestDto(Integer page, Integer size) {
		this.page = page > 0 ? page : 1;
		this.size = size > 0 ? size : 5;
	}

	public Pageable getPageable() {
		return PageRequest.of(page - 1, size);
	}
}
