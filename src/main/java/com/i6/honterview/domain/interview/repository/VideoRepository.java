package com.i6.honterview.domain.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i6.honterview.domain.interview.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
