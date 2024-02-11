package com.i6.honterview.repository;

import org.springframework.data.repository.CrudRepository;

import com.i6.honterview.domain.redis.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
