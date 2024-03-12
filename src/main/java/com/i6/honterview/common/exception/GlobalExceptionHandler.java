package com.i6.honterview.common.exception;

import static com.i6.honterview.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.i6.honterview.common.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
		log.warn(">>>>> CustomException : {}", ex.getMessage());
		ErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
	}

	@ExceptionHandler(SecurityCustomException.class)
	protected ResponseEntity<ErrorResponse> handleSecurityCustomException(SecurityCustomException ex) {
		log.warn(">>>>> SecurityCustomException : {}", ex.getMessage());
		SecurityErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
	}

	@ExceptionHandler(OpenAiException.class)
	protected ResponseEntity<ErrorResponse> handleOpenAiException(OpenAiException ex) {
		ErrorResponse response = new ErrorResponse(ex.getCode(), ex.getMessage());
		log.warn(">>>>> OpenAiException : {}", response.getErrorMessage());
		return ResponseEntity.status(ex.getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.warn(">>>>> validation Failed : {}", ex.getMessage());
		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		ErrorResponse errorResponse = INVALID_INPUT_VALUE.getErrorResponse();
		fieldErrors.forEach(error -> errorResponse.addValidation(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error(">>>>> Internal Server Error : {}", ex);
		ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
