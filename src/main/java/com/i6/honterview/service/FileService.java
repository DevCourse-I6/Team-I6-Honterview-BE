package com.i6.honterview.service;

import java.net.URL;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Video;
import com.i6.honterview.dto.response.DownloadUrlResponse;
import com.i6.honterview.dto.response.UploadUrlResponse;
import com.i6.honterview.exception.CustomException;
import com.i6.honterview.exception.ErrorCode;
import com.i6.honterview.repository.InterviewRepository;
import com.i6.honterview.repository.VideoRepository;
import com.i6.honterview.util.FileUtils;

import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private final VideoRepository videoRepository;
	private final InterviewRepository interviewRepository;
	private final S3Template s3Template;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String s3Bucket;

	public UploadUrlResponse generateUploadUrl(Long interviewId, Long memberId) {
		Interview interview = interviewRepository.findById(interviewId)    // TODO interviewService로 분리
			.orElseThrow(() -> new CustomException(ErrorCode.INTERVIEW_NOT_FOUND));

		if (!interview.isSameInterviewee(memberId)) {
			throw new CustomException(ErrorCode.INTERVIEWEE_NOT_SAME);
		}

		String fileName = FileUtils.generateFileName();
		try {
			URL url = s3Template.createSignedPutURL(s3Bucket, fileName, Duration.ofMinutes(10));
			videoRepository.save(new Video(fileName)); // TODO: answer과 연관지어야 함
			return new UploadUrlResponse(url.toString());
		} catch (S3Exception e) {
			log.error("createSignedPutURL failed : ", e);
			throw new CustomException(ErrorCode.GENERATE_UPLOAD_URL_FAILED);
		}
	}

	public DownloadUrlResponse generateDownloadUrl(Long videoId) {
		Video video = videoRepository.findById(videoId)
			.orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));

		try {
			URL url = s3Template.createSignedGetURL(s3Bucket, video.getFileName(), Duration.ofMinutes(10));
			return new DownloadUrlResponse(url.toString());
		} catch (S3Exception e) {
			log.error("createSignedGetURL failed : ", e);
			throw new CustomException(ErrorCode.GENERATE_DOWNLOAD_URL_FAILED);
		}
	}
}
