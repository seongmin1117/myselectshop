package com.sparta.myselectshop.auth.security;

import com.sparta.myselectshop.auth.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest req,
      @NonNull HttpServletResponse res,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String tokenValue = jwtUtil.getJwtFromHeader(req);
    log.info("🔍 요청에서 JWT 토큰 추출: {}", tokenValue);

    if (StringUtils.hasText(tokenValue)) {
      log.info("✅ JWT 토큰이 존재합니다. 검증을 시작합니다.");

      if (!jwtUtil.validateToken(tokenValue)) {
        log.error("❌ JWT 검증 실패: 유효하지 않은 토큰입니다.");
        return;
      }

      Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
      log.info("🔑 JWT에서 사용자 정보 추출: {}", info.getSubject());

      try {
        setAuthentication(info.getSubject());
        log.info("✅ 사용자 '{}' 인증 성공", info.getSubject());
      } catch (Exception e) {
        log.error("⚠️ 인증 처리 중 예외 발생: {}", e.getMessage());
        return;
      }
    } else {
      log.info("⚠️ JWT 토큰이 요청에 존재하지 않습니다. 인증을 건너뜁니다.");
    }

    filterChain.doFilter(req, res);
  }

  // 인증 처리
  public void setAuthentication(String username) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(username);
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    log.info("🔐 SecurityContext에 '{}' 사용자 인증 정보 설정 완료", username);
  }

  // 인증 객체 생성
  private Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    log.info("👤 사용자 '{}' 정보 로드 완료", username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
