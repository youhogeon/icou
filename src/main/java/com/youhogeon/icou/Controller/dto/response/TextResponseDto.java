package com.youhogeon.icou.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TextResponseDto {

    private String text;
    private long expiredAfterSeconds;

}
