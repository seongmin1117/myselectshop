package com.sparta.myselectshop.user.presentation;

import com.sparta.myselectshop.auth.security.UserDetailsImpl;
import com.sparta.myselectshop.user.application.UserService;
import com.sparta.myselectshop.user.application.dto.SignupRequest;
import com.sparta.myselectshop.user.application.dto.UserInfo;
import com.sparta.myselectshop.user.domain.Role;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  @GetMapping("/user/login-page")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/user/signup")
  public String signupPage() {
    return "signup";
  }

  @PostMapping("/user/signup")
  public String signup(@Valid SignupRequest request, BindingResult bindingResult) {
    // Validation 예외처리
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    if (!fieldErrors.isEmpty()) {
      for (FieldError fieldError : bindingResult.getFieldErrors()) {
        log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
      }
      return "redirect:/api/user/signup";
    }

    userService.signup(request);

    return "redirect:/api/user/login-page";
  }

  // 회원 관련 정보 받기
  @GetMapping("/user-info")
  @ResponseBody
  public UserInfo getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    String username = userDetails.user().getUsername();
    Role role = userDetails.user().getRole();
    boolean isAdmin = (role == Role.ADMIN);

    return new UserInfo(username, isAdmin);
  }
}
