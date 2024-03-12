package com.i6.honterview.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 유니크한 파일 이름을 생성합니다
 *
 * @return ex) honterview_202403040929137878.webm
 */
public class FileNameUtil {

	private static final String FILENAME_PREFIX = "honterview_";
	private static final String FILE_EXTENSTION = ".webm";

	private FileNameUtil() {
	}

	public static String generateFileName() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String timestamp = now.format(formatter);

		return FILENAME_PREFIX + timestamp + FILE_EXTENSTION;
	}
}
