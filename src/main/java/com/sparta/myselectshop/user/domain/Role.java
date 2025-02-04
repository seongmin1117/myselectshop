package com.sparta.myselectshop.user.domain;

import lombok.Getter;

@Getter
public enum Role {
  USER(Authority.USER), // 사용자 권한
  ADMIN(Authority.ADMIN); // 관리자 권한

  private final String authority;

  Role(String authority) {
    this.authority = authority;
  }

  // Spring Security 호환성을 위해 설정
  public static class Authority {
    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";
  }
}
