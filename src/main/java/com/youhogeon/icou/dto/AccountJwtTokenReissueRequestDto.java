package com.youhogeon.icou.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountJwtTokenReissueRequestDto {
    
    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

}
