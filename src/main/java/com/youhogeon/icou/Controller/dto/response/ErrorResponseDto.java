package com.youhogeon.icou.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponseDto {

    private int status;
    private String code;
    private String message;

}