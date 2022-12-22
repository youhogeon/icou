package com.youhogeon.icou.controller.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {
    
    private String nickname;
    private String comment;
    private LocalDateTime createdAt;

}
