package com.i6.honterview.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {

	private static final String FILENAME_PREFIX = "honterview_";
	private static final String FILE_EXTENSTION = ".webm";

	private FileUtils() {
	}

	/**
	 * 유니크한 파일 이름을 생성합니다
	 *
	 * @return ex) honterview_202403040929137878.mp4
	 */

	public static String generateFileName() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String timestamp = now.format(formatter);

		return FILENAME_PREFIX + timestamp + FILE_EXTENSTION;
	}
}
