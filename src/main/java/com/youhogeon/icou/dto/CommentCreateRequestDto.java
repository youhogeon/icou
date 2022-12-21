package com.youhogeon.icou.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {
    
    @NotBlank(message = "comment는 비어있을 수 없습니다.")
    private String comment;
    
}
