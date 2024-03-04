package com.i6.honterview.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.dto.request.FileUploadRequest;
import com.i6.honterview.dto.response.FileUploadResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "파일")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@Operation(summary = "파일 업로드", description = "파일을 S3에 업로드합니다.")
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile( // TODO : answer와 연결
		@RequestPart("request") FileUploadRequest request,
		@RequestPart("file") MultipartFile file
	) {
		FileUploadResponse response = fileService.uploadFile(request, file);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/v1/files/download/{recordId}")
			.buildAndExpand(response.recordId())
			.toUri();

		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}

	@Operation(summary = "파일 다운로드", description = "S3로부터 파일을 다운로드 합니다.")
	@GetMapping("/download/{recordId}")
	public ResponseEntity<Resource> downloadFile(
		@PathVariable Long recordId,
		HttpServletResponse response
	) {
		Resource resource = fileService.downloadFile(recordId);

		// 브라우저에서 파일을 다운로드하기 위한 헤더 추가
		// ex) Content-Disposition: attachment; filename="example.mp3"
		ContentDisposition contentDisposition = ContentDisposition.attachment()
			.filename(resource.getFilename(), StandardCharsets.UTF_8)
			.build();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(resource);
	}
}
