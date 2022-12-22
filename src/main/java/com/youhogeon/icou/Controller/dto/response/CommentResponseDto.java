package com.youhogeon.icou.controller.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentResponseDto {
    
    private List<CommentDto> comments;

}
