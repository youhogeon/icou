package com.youhogeon.icou.error;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(BusinessException e) {
        e.printStackTrace();

        return error(e.getErrorCode());
    }
    
    @ExceptionHandler({
        NoHandlerFoundException.class,
        NotFoundException.class,
        HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        e.printStackTrace();

        return error(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler({
        InsufficientAuthenticationException.class,
        UnauthorizedException.class
    })
    public ResponseEntity<?> handleUnauthorizedException(Exception e) {
        e.printStackTrace();

        return error(ErrorCode.FORBIDDEN);
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
        HttpMessageNotReadableException.class,
        IllegalArgumentException.class,
        IllegalStateException.class,
    })
    public ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        e.printStackTrace();

        return error(ErrorCode.ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        e.printStackTrace();

        return error(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> handleHttpMediaTypeException(Exception e) {
        e.printStackTrace();

        return error(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        e.printStackTrace();

        return error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
