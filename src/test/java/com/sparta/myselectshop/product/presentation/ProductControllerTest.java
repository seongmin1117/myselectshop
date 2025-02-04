package com.sparta.myselectshop.product.presentation;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.product.application.ProductService;
import com.sparta.myselectshop.product.application.dto.MyPriceUpdateRequest;
import com.sparta.myselectshop.product.application.dto.ProductCreateRequest;
import com.sparta.myselectshop.product.application.dto.ProductListResponse;
import com.sparta.myselectshop.product.application.dto.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductController.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
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
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @DisplayName("관심상품 리스트를 조회할 수 있다.")
  @Test
  void getProducts() throws Exception {
    // given
    List<ProductResponse> products = List.of();
    ProductListResponse result = new ProductListResponse(products);
    when(productService.getProducts()).thenReturn(result);

    // when & then
    mockMvc
        .perform(get("/api/v1/products").contentType(MediaType.APPLICATION_JSON))
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
