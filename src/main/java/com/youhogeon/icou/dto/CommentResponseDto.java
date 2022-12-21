package com.youhogeon.icou.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentResponseDto {
    
    private List<CommentDto> comments;

}
