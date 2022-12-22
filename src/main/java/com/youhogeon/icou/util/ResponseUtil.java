package com.youhogeon.icou.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.youhogeon.icou.controller.dto.response.ErrorResponseDto;
import com.youhogeon.icou.controller.dto.response.ResponseDto;
import com.youhogeon.icou.error.ErrorCode;

public class ResponseUtil {

    public static <T> ResponseDto<T> ok(T response) {
        return new ResponseDto<>(HttpStatus.OK, response);
    }

    public static ResponseDto<?> ok() {
        return ok(null);
    }

    public static ResponseEntity<ErrorResponseDto> error(ErrorCode errorCode) {
        HttpStatus httpStatus = errorCode.getStatus();

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(httpStatus.value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        return new ResponseEntity<>(errorResponseDto, httpStatus);
    }

    public static ResponseEntity<ErrorResponseDto> error(ErrorCode errorCode, String message) {
        HttpStatus httpStatus = errorCode.getStatus();

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(httpStatus.value())
                .code(errorCode.name())
                .message(message)
                .build();

        return new ResponseEntity<>(errorResponseDto, httpStatus);
    }

    public static ResponseEntity<ErrorResponseDto> error(HttpStatus httpStatus, String message) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(httpStatus.value())
                .code(httpStatus.name())
                .message(message)
                .build();

        return new ResponseEntity<>(errorResponseDto, httpStatus);
    }

    public static ResponseEntity<ErrorResponseDto> error(HttpStatus status, Throwable throwable) {
        return error(status, throwable.toString());
    }

    public static ResponseEntity<ErrorResponseDto> error(HttpStatus status) {
        return error(status, status.getReasonPhrase());
    }

}