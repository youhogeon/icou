package com.youhogeon.icou.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> Response<T> ok(T response) {
        return new Response<>(HttpStatus.OK, response);
    }

    public static Response<?> ok() {
        return new Response<>(HttpStatus.OK, null);
    }

    public static ResponseEntity<Response<String>> error(HttpStatus status, Throwable throwable) {
        return error(status, throwable.toString());
    }

    public static ResponseEntity<Response<String>> error(HttpStatus status, String message) {
        return new ResponseEntity<>(new Response<>(status, message), status);
    }

    public static ResponseEntity<Response<String>> error(HttpStatus status) {
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