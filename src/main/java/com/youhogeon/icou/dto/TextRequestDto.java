package com.youhogeon.icou.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TextRequestDto {
    
    @NotBlank
    private String text;
    
}
