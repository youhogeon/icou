package com.youhogeon.icou.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenResponseDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpiresIn;

}
