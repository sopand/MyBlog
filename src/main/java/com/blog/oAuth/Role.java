package com.blog.oAuth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER","유저"),
    ROLE_ADMIN("ROLE_ADMIN","관리자");

    private final String role;
    private final String title;

}
