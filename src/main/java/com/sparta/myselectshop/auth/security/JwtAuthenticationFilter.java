package com.sparta.myselectshop.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.auth.application.dto.LoginRequest;
import com.sparta.myselectshop.auth.jwt.JwtUtil;
import com.sparta.myselectshop.user.domain.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
    setFilterProcessesUrl("/api/user/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequest requestDto =
          new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

      log.info("로그인 시도 - 사용자: {}", requestDto.username());
      return getAuthenticationManager()
          .authenticate(
              new UsernamePasswordAuthenticationToken(
                  requestDto.username(), requestDto.password(), null));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
    Role role = ((UserDetailsImpl) authResult.getPrincipal()).user().getRole();

    String token = jwtUtil.createToken(username, role);
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    log.info("로그인 성공 - 사용자: {}, 역할: {}", username, role);
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(401);
    log.error("로그인 실패 - 이유: {}", failed.getMessage());
  }
}
