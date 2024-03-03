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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.dto.response.FileUploadResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.FileService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<FileUploadResponse>> uploadRecordFile( // TODO : answerId와 연결
		@RequestParam("file") MultipartFile file
	) {
		FileUploadResponse response = fileService.uploadRecordFile(file);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/v1/files/download/{recordId}")
			.buildAndExpand(response.recordId())
			.toUri();

		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}

	@GetMapping("/download/{recordId}")
	public ResponseEntity<Resource> downloadRecordFile(
		@PathVariable Long recordId,
		HttpServletResponse response
	) {
		Resource resource = fileService.downloadRecordFile(recordId);

		ContentDisposition contentDisposition = ContentDisposition.attachment()
			.filename(resource.getFilename(), StandardCharsets.UTF_8)
			.build();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_OCTET_STREAM)
			.body(resource);
	}
}
