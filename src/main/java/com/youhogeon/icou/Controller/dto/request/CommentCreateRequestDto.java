package com.youhogeon.icou.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    
    @NotBlank(message = "comment는 비어있을 수 없습니다.")
    private String comment;
    
}
