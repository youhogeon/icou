package com.youhogeon.icou.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AccountJwtTokenReissueRequestDto {
    
    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

}
