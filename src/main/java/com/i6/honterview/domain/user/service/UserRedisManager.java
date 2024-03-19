package com.i6.honterview.domain.user.service;

import org.springframework.stereotype.Component;

import com.i6.honterview.common.exception.SecurityCustomException;
import com.i6.honterview.common.exception.SecurityErrorCode;
import com.i6.honterview.common.util.RedisOperator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRedisManager {

	private static final int REFRESH_TOKEN_EXPIRATION_SECONDS = 604800;
	private static final int BLACKLIST_EXPIRATION_SECONDS = 1800;
	private final RedisOperator redisOperator;

	public void saveRefreshToken(String key, Object value) {
		redisOperator.saveWithExpiration(key, value, REFRESH_TOKEN_EXPIRATION_SECONDS);
	}

	public Object get(String key) {
		return redisOperator.get(key);
	}

	public void delete(String key) {
		redisOperator.delete(key);
	}

	public boolean existsRefresh(String key) {
		return redisOperator.exists(key);
	}

	public void saveBlackList(String key, Object value) {
		redisOperator.saveWithExpiration(key, value, BLACKLIST_EXPIRATION_SECONDS);
	}

	public void validateBlackList(String key) {
		if (redisOperator.hasKeyBlackList(key)) {
			throw new SecurityCustomException(SecurityErrorCode.ALREADY_LOGGED_OUT);
		}
	}
}
