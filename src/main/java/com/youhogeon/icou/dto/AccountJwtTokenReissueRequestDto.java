package com.youhogeon.icou.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountJwtTokenReissueRequestDto {
    
    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

}
