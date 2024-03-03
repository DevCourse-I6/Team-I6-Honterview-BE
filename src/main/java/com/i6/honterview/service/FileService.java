package com.i6.honterview.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.i6.honterview.domain.Record;
import com.i6.honterview.dto.response.FileUploadResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.RecordRepository;

import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList(".mp3", ".m4a");

	private final RecordRepository recordRepository;
	private final S3Template s3Template;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String s3Bucket;

	public FileUploadResponse uploadRecordFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();

		if (!isFileExtensionAllowed(fileName)) {
			throw new CustomException(ErrorCode.INVALID_FILE_FORMAT);
		}

		try (InputStream inputStream = file.getInputStream()) {
			S3Resource resource = s3Template.upload(s3Bucket, fileName, inputStream);
			Record record = recordRepository.save(new Record(resource.getFilename()));
			log.info("File uploaded successfully: {}", record);
			return FileUploadResponse.from(record);
		} catch (IOException e) {
			log.error("IOException : ", e);
			throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
		} catch (S3Exception e) {
			log.error("S3 upload failed : ", e);
			throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
		}
	}

	private boolean isFileExtensionAllowed(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf("."));
		return ALLOWED_FILE_EXTENSIONS.contains(extension.toLowerCase());
	}

	public Resource downloadRecordFile(Long recordId) {
		try {
			Record record = recordRepository.findById(recordId)
				.orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
			return s3Template.download(s3Bucket, record.getFileName());
		} catch (S3Exception e) {
			log.error("S3 download failed : ", e);
			throw new CustomException(ErrorCode.FILE_DOWNLOAD_FAILED);
		}
	}
}
