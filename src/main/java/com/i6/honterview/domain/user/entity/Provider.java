package com.i6.honterview.domain.user.entity;

import java.util.Arrays;

public enum Provider {
	KAKAO, GITHUB, GOOGLE;

	public static Provider getProviderFromString(String input) {
		return Arrays.stream(Provider.values())
			.filter(provider -> provider.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No matching enum constant for input: " + input));
	}
}
