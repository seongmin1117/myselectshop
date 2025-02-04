package com.sparta.myselectshop.user.application.dto;

import com.sparta.myselectshop.auth.security.UserDetailsImpl;

public record UserInfo(String username, Boolean isAdmin) {
  public static UserInfo of(final UserDetailsImpl userDetails) {
    return new UserInfo(userDetails.getUsername(), userDetails.isAdmin());
  }
}
