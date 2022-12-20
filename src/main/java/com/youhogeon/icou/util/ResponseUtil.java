package com.youhogeon.icou.util;

import org.springframework.http.HttpStatus;

public class ResponseUtil {

    public static <T> Response<T> success(T response) {
        return new Response<>(HttpStatus.OK, response);
    }

    public static Response<?> success() {
        return new Response<>(HttpStatus.OK, null);
    }

    public static Response<String> error(HttpStatus status, Throwable throwable) {
        return new Response<>(status, throwable.toString());
    }

    public static Response<String> error(HttpStatus status, String message) {
        return new Response<>(status, message);
    }

    public static Response<?> error(HttpStatus status) {
        return new Response<>(status, null);
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