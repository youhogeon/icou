package com.youhogeon.icou.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());

        this.errorCode = errorCode;
    }

}