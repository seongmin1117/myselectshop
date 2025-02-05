package com.sparta.myselectshop.product.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sparta.myselectshop.common.ApiTest;
import com.sparta.myselectshop.product.application.ProductService;
import com.sparta.myselectshop.product.application.dto.MyPriceUpdateRequest;
import com.sparta.myselectshop.product.application.dto.ProductCreateRequest;
import com.sparta.myselectshop.product.application.dto.ProductListResponse;
import com.sparta.myselectshop.product.application.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest extends ApiTest {

  @MockitoBean private ProductService productService;

  @DisplayName("관심상품을 등록할 수 있다.")
  @Test
  void create() throws Exception {
    // given
    ProductCreateRequest request = createProduct("title1", "image1", "link1", 10000);

    // when & then
    mockMvc
        .perform(
            post("/api/v1/products")
                .header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE)
                .with(csrf()) // 시큐리티6 부터 필요해짐 403방지
                .with(getUserRequestPostProcessor()) // 401방지
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @DisplayName("유저별 관심상품을 조회할 수 있다.")
  @Test
  void getProductsByUser() throws Exception {
    // given
    Page<ProductResponse> products = Page.empty();
    ProductListResponse result = new ProductListResponse(products);

    when(productService.getProductsByUser(any(), anyInt(), anyInt(), anyString(), anyBoolean()))
        .thenReturn(result);

    // when & then
    mockMvc
        .perform(
            get("/api/v1/products")
                .header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE)
                .with(getUserRequestPostProcessor())
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "lprice")
                .param("isAsc", "true")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @DisplayName("희망최저가를 수정할 수 있다.")
  @Test
  void updateMyPrice() throws Exception {
    // given
    MyPriceUpdateRequest request = new MyPriceUpdateRequest(10000);
    // when & then
    mockMvc
        .perform(
            put("/api/v1/products")
                .header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE)
                .with(csrf())
                .with(getUserRequestPostProcessor())
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1")
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  private ProductCreateRequest createProduct(
      String title, String image, String link, Integer lprice) {
    return new ProductCreateRequest(title, image, link, lprice);
  }
}
