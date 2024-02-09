package com.i6.honterview.domain.enums;

import java.util.Arrays;

public enum Provider {
	KAKAO, NAVER, GITHUB;

	public static Provider getProviderFromString(String input) {
		return Arrays.stream(Provider.values())
			.filter(provider -> provider.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No matching enum constant for input: " + input));
	}
}
