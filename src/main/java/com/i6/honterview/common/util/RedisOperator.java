package com.i6.honterview.common.util;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.i6.honterview.common.exception.CustomException;
import com.i6.honterview.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisOperator {

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;
	private final RedisTemplate<Long, Long> redisCountTemplate;

	public Object get(String key) {
		return tryOperation(() -> redisTemplate.opsForValue().get(key));
	}

	public void delete(String key) {
		tryOperation(() -> redisTemplate.delete(key));
	}

	public Boolean exists(String key) {
		return tryOperation(() -> redisTemplate.hasKey(key));
	}

	public boolean hasKeyBlackList(String key) {
		return tryOperation(() -> redisBlackListTemplate.hasKey(key));
	}

	public void saveWithExpiration(String key, Object value, long expirationSeconds) {
		Duration timeout = Duration.ofSeconds(expirationSeconds);
		tryOperation(() -> {
			redisTemplate.opsForValue().set(key, value, timeout);
			redisTemplate.expire(key, timeout);
			return null;
		});
	}

	public void deleteGptCount(Long key) {
		tryOperation(() -> redisCountTemplate.delete(key));
	}

	public Long increaseCount(Long key) {
		 return tryOperation(() -> redisCountTemplate.opsForValue().increment(key, 1));
	}

	private <T> T tryOperation(Supplier<T> operation) {
		try {
			return operation.get();
		} catch (Exception e) {
			log.error("RedisManager occurred: " + e.getMessage(), e);
			throw new CustomException(ErrorCode.SERVICE_UNAVAILABLE);
		}
	}
}
