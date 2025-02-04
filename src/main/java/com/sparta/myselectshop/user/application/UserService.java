package com.sparta.myselectshop.user.application;

import com.sparta.myselectshop.user.application.dto.SignupRequest;
import com.sparta.myselectshop.user.domain.Role;
import com.sparta.myselectshop.user.domain.User;
import com.sparta.myselectshop.user.infrastructure.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // ADMIN_TOKEN
  private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

  public void signup(SignupRequest request) {
    String username = request.username();
    String password = passwordEncoder.encode(request.password());

    // 회원 중복 확인
    Optional<User> checkUsername = userRepository.findByUsername(username);
    if (checkUsername.isPresent()) {
      throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
    }

    // email 중복확인
    String email = request.email();
    Optional<User> checkEmail = userRepository.findByEmail(email);
    if (checkEmail.isPresent()) {
      throw new IllegalArgumentException("중복된 Email 입니다.");
    }

    // 사용자 ROLE 확인
    Role role = Role.USER;
    if (request.isAdmin()) {
      if (!ADMIN_TOKEN.equals(request.adminToken())) {
        throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
      }
      role = Role.ADMIN;
    }

    // 사용자 등록
    User user = new User(username, password, email, role);
    userRepository.save(user);
  }
}
