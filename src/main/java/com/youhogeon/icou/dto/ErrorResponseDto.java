package com.youhogeon.icou.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponseDto {

    private int status;
    private String code;
    private String message;

}