package com.i6.honterview.common.util;

import java.util.List;
import java.util.Random;

/**
 * 형용사와 동물의 이름을 조합하여 랜덤으로 닉네임을 생성합니다.
 */
public class NicknameGeneratorUtil {

	private static final Random RANDOM = new Random();

	private static final List<String> ADJECTIVES = List.of(
		"노력하는", "긍정적인", "활기찬", "사려깊은", "용감한", "재미있는", "친절한", "빛나는", "매력적인", "기분좋은",
		"똑똑한", "명랑한", "지혜로운", "영리한", "우아한", "자신감있는", "행복한", "열정적인", "신비로운", "창의적인"
	);

	private static final List<String> NOUNS = List.of(
		"사자", "토끼", "고양이", "여우", "호랑이", "늑대", "판다", "기린", "코끼리", "하마",
		"다람쥐", "라마", "강아지", "수달", "돌고래", "펭귄", "쿼카", "사슴", "거북이", "코알라"
	);

	private NicknameGeneratorUtil() {
	}

	public static String generateRandomNickname() {
		String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
		String noun = NOUNS.get(RANDOM.nextInt(NOUNS.size()));
		return adjective + " " + noun;
	}

}
