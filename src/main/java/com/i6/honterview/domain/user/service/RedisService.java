package com.i6.honterview.domain.user.service;

import org.springframework.stereotype.Service;

import com.i6.honterview.domain.user.repository.RedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final int REFRESH_TOKEN_EXPIRATION_SECONDS = 604800;
	private static final int BLACKLIST_EXPIRATION_SECONDS = 1800;
	private final RedisRepository redisRepository;

	public void saveRefreshToken(String key, Object value) {
		redisRepository.saveWithExpiration(key, value, REFRESH_TOKEN_EXPIRATION_SECONDS);
	}

	public Object get(String key) {
		return redisRepository.get(key);
	}

	public void delete(String key) {
		redisRepository.delete(key);
	}

	public boolean hasKey(String key) {
		return redisRepository.exists(key);
	}

	public void saveBlackList(String key, Object value) {
		redisRepository.saveWithExpiration(key, value, BLACKLIST_EXPIRATION_SECONDS);
	}

	public boolean hasKeyBlackList(String key) {
		return redisRepository.hasKeyBlackList(key);
	}
}
