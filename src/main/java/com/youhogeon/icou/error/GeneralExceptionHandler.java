package com.youhogeon.icou.error;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

import static com.youhogeon.icou.util.ResponseUtil.error;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(BusinessException e) {
        log.info("BusinessException: {}", e.getMessage());

        return error(e.getErrorCode());
    }
    
    @ExceptionHandler({
        NoHandlerFoundException.class,
        NotFoundException.class,
    })
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        log.info("Not Found Exception: {}", e.getMessage());

        return error(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler({
        InsufficientAuthenticationException.class,
        UnauthorizedException.class,
    })
    public ResponseEntity<?> handleUnauthorizedException(Exception e) {
        log.info("Authrization Exception: {}", e.getMessage());

        return error(ErrorCode.INVALID_JWT_TOKEN);
    }

    @ExceptionHandler({
        AccessDeniedException.class,
    })
    public ResponseEntity<?> handleForbiddenException(Exception e) {
        log.info("Access Denied Exception: {}", e.getMessage());

        return error(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler({
        BindException.class,
        MethodArgumentNotValidException.class,
    })
    public ResponseEntity<?> handleBindException(BindException e) {
        log.info("Invalid Argument Exception: {}", e.getMessage());

        return error(
            ErrorCode.ILLEGAL_ARGUMENT, 
            e.getFieldErrors().get(0).getDefaultMessage()
        );
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        IllegalArgumentException.class,
        IllegalStateException.class,
    })
    public ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        log.info("Illegal Argument Exception: {}", e.getMessage());

        return error(ErrorCode.ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler({
        HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        log.info("Method Exception: {}", e.getMessage());

        return error(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> handleHttpMediaTypeException(Exception e) {
        log.info("Media Type Exception: {}", e.getMessage());

        return error(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unknown Exception: {}", e.getMessage());

        return error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
