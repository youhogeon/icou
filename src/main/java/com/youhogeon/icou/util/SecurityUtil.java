package com.youhogeon.icou.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.youhogeon.icou.error.InvalidTokenException;

public class SecurityUtil {

    public static Long getCurrentAccountId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new InvalidTokenException();
        }

        if (authentication.getName().equals("anonymousUser")) return null;

        return Long.parseLong(authentication.getName());
    }

}