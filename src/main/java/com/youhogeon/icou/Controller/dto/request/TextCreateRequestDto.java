package com.youhogeon.icou.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TextCreateRequestDto {
    
    @NotBlank(message = "text는 비어있을 수 없습니다.")
    private String text;
    
}
