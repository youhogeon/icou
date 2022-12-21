package com.youhogeon.icou.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.youhogeon.icou.dto.ErrorResponseDto;
import com.youhogeon.icou.error.ErrorCode;

public class ResponseUtil {

    public static <T> Response<T> ok(T response) {
        return new Response<>(HttpStatus.OK, response);
    }

    public static Response<?> ok() {
        return new Response<>(HttpStatus.OK, null);
    }

    public static ResponseEntity<Response<ErrorResponseDto>> error(ErrorCode errorCode) {
        HttpStatus httpStatus = errorCode.getStatus();

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        Response<ErrorResponseDto> response = new Response<>(httpStatus, errorResponseDto);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<Response<ErrorResponseDto>> error(HttpStatus httpStatus, String message) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(message)
                .build();

        Response<ErrorResponseDto> response = new Response<>(httpStatus, errorResponseDto);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<Response<ErrorResponseDto>> error(HttpStatus status, Throwable throwable) {
        return error(status, throwable.toString());
    }

    public static ResponseEntity<Response<ErrorResponseDto>> error(HttpStatus status) {
        return error(status, status.getReasonPhrase());
    }

    public static class Response<T> {

        private final HttpStatus status;
        private final T response;

        private Response(HttpStatus status, T response) {
            this.status = status;
            this.response = response;
        }

        public int getStatus() {
            return status.value();
        }

        public T getResponse() {
            return response;
        }

    }

}