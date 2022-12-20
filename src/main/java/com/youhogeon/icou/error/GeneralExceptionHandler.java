package com.youhogeon.icou.error;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.youhogeon.icou.util.ResponseUtil.error;

@RestControllerAdvice
public class GeneralExceptionHandler {
    
    @ExceptionHandler({
        NoHandlerFoundException.class,
        NotFoundException.class,
        HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        e.printStackTrace();

        return error(HttpStatus.NOT_FOUND, "API를 찾을 수 없습니다.");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(Exception e) {
        e.printStackTrace();

        return error(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

    @ExceptionHandler({
        InvalidTokenException.class,
        InsufficientAuthenticationException.class
    })
    public ResponseEntity<?> handleAuthenticationException(Exception e) {
        e.printStackTrace();

        return error(
            HttpStatus.UNAUTHORIZED,
            e.getMessage()
        );
    }

    @ExceptionHandler({
        BindException.class
    })
    public ResponseEntity<?> handleBindException(BindException e) {
        e.printStackTrace();

        return error(
            HttpStatus.BAD_REQUEST, 
            e.getFieldErrors().get(0).getDefaultMessage()
        );
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
    })
    public ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        e.printStackTrace();

        return error(
            HttpStatus.BAD_REQUEST, 
            e.getMessage()
        );
    }

    @ExceptionHandler({
        IllegalStateException.class,
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        e.printStackTrace();

        return error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> handleHttpMediaTypeException(Exception e) {
        e.printStackTrace();

        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "잘못된 요청입니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        e.printStackTrace();

        return error(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");
    }

}
