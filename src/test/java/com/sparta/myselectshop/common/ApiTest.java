package com.sparta.myselectshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.auth.security.UserDetailsImpl;
import com.sparta.myselectshop.user.domain.Role;
import com.sparta.myselectshop.user.domain.User;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@WebMvcTest
@MockitoBean(types = JpaMetamodelMappingContext.class)
public abstract class ApiTest {
  protected static final String AUTHORIZATION_HEADER_KEY = "Authorization";
  protected static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaa.bbbb.cccc";
  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;
  protected UserDetailsImpl mockUserDetails;

  @BeforeEach
  void setUp() throws ServletException, IOException {
    // 공통적으로 사용할 가짜 사용자 생성
    User mockUser = new User("mockUser", "password", "mockuser@.com", Role.USER);
    mockUserDetails = new UserDetailsImpl(mockUser);
  }

  // API 테스트에서 쉽게 사용할 수 있도록 RequestPostProcessor 반환
  protected RequestPostProcessor getUserRequestPostProcessor() {
    return SecurityMockMvcRequestPostProcessors.user(mockUserDetails);
  }
}
