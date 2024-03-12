package com.i6.honterview.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.i6.honterview.repository.RedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 7;
	private static final int BLACKLIST_EXPIRATION_MINUTES = 30;
	private final RedisRepository redisRepository;

	public void saveRefreshToken(String key, Object value) {
		redisRepository.saveWithExpiration(key, value, REFRESH_TOKEN_EXPIRATION_DAYS, TimeUnit.DAYS);
	}

	public Object get(String key) {
		return redisRepository.get(key);
	}

	public void delete(String key) {
		redisRepository.delete(key);
	}

	public boolean hasKey(String key) {
		return redisRepository.hasKey(key);
	}

	public void saveBlackList(String key, Object value) {
		redisRepository.saveWithExpiration(key, value, BLACKLIST_EXPIRATION_MINUTES, TimeUnit.MINUTES);
	}

	public boolean hasKeyBlackList(String key) {
		return redisRepository.hasKeyBlackList(key);
	}
}
