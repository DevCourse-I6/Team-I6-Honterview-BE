package com.i6.honterview.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisRepository {

	private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 7;
	private static final int BLACKLIST_EXPIRATION_MINUTES = 30;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;

	public void saveRefreshToken(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
		redisTemplate.expire(key, REFRESH_TOKEN_EXPIRATION_DAYS, TimeUnit.DAYS);
	}

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	public void saveBlackList(String key, Object value) {
		ValueOperations<String, Object> valueOperations = redisBlackListTemplate.opsForValue();
		valueOperations.set(key, value);
		redisBlackListTemplate.expire(key, BLACKLIST_EXPIRATION_MINUTES, TimeUnit.MINUTES);
	}

	public boolean hasKeyBlackList(String key) {
		return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
	}
}
