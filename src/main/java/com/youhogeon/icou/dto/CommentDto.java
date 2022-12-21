package com.youhogeon.icou.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDto {
    
    private String nickname;
    private String comment;
    private LocalDateTime createdAt;

}
