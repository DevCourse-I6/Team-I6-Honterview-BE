package com.i6.honterview.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class FileUtils {

	private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".m4a", ".mp3"); // TODO 프론트랑 파일 확장자 정하기
	private static final String FILENAME_PREFIX = "honterview_";

	private FileUtils() {
	}

	/**
	 * 유니크한 파일 이름을 생성합니다
	 *
	 * @param originalFilename 기존 파일명
	 * @return ex) honterview_202403040929137878.mp3
	 */

	public static String generateFileName(String originalFilename) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String timestamp = now.format(formatter);

		String extension = "";
		int dotIndex = originalFilename.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
			extension = originalFilename.substring(dotIndex);
		}

		return FILENAME_PREFIX + timestamp + extension;
	}

	/**
	 * 파일 확장자 검사 메소드
	 *
	 * @param filename 파일명
	 * @return 허용된 확장자일 경우 true
	 */

	public static boolean isFileExtensionAllowed(String filename) {
		String extension = "";
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < filename.length() - 1) {
			extension = filename.substring(dotIndex);
		}
		return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
	}
}
