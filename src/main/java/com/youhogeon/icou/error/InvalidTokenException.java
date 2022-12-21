package com.youhogeon.icou.error;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_JWT_TOKEN);
    }

}