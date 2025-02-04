package com.sparta.myselectshop.product.application;

import static org.assertj.core.api.Assertions.*;

import com.sparta.myselectshop.product.application.dto.MyPriceUpdateRequest;
import com.sparta.myselectshop.product.application.dto.ProductCreateRequest;
import com.sparta.myselectshop.product.application.dto.ProductListResponse;
import com.sparta.myselectshop.product.application.dto.ProductResponse;
import com.sparta.myselectshop.product.domain.Product;
import com.sparta.myselectshop.product.domain.vo.MyPrice;
import com.sparta.myselectshop.product.infrastructure.ProductRepository;
import com.sparta.myselectshop.user.domain.Role;
import com.sparta.myselectshop.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {
  @Autowired ProductService productService;
  @Autowired ProductRepository productRepository;

  @DisplayName("관심상품을 생성할 수 있다.")
  @Test
  void createProduct() {
    // given
    ProductCreateRequest product = createProduct("title1", "image1", "link1", 10000);
    User user = new User("username", "password", "email", Role.USER);
    // when
    ProductResponse response = productService.create(product, user);
    // then
    assertThat(response)
        .extracting("title", "image", "link", "lprice", "myPrice")
        .contains("title1", "image1", "link1", 10000, 0);
  }

  @DisplayName("관심상품을 조회할 수 있다.")
  @Test
  void getProducts() {
    // given
    User user = new User("username", "password", "email", Role.USER);
    ProductCreateRequest product1 = createProduct("title1", "image1", "link1", 10000);
    ProductCreateRequest product2 = createProduct("title2", "image2", "link2", 20000);
    ProductCreateRequest product3 = createProduct("title3", "image3", "link3", 30000);
    productRepository.save(ProductCreateRequest.toEntity(product1, user));
    productRepository.save(ProductCreateRequest.toEntity(product2, user));
    productRepository.save(ProductCreateRequest.toEntity(product3, user));
    // when
    ProductListResponse response = productService.getProducts();
    // then
    assertThat(response.products()).hasSize(3);
  }

  @DisplayName("관심상품의 희망 최저가를 업데이트 할 수 있다. ")
  @Test
  void updateMyPrice() {
    // given
    User user = new User("username", "password", "email", Role.USER);
    ProductCreateRequest product = createProduct("title1", "image1", "link1", 10000);
    Product original = productRepository.save(ProductCreateRequest.toEntity(product, user));
    MyPriceUpdateRequest request = new MyPriceUpdateRequest(2000);
    // when
    productService.updateMyPrice(original.getId(), request);
    // then
    Optional<Product> changedResult = productRepository.findById(original.getId());
    assertThat(changedResult).isPresent();
    assertThat(changedResult.get().getMyPrice().getValue()).isEqualTo(2000);
  }

  @DisplayName("희망 최저가가 비즈니스 요구사항 가격 보다 낮을 경우 예외가 발생한다.")
  @Test
  void validateMyPrice() {
    // given
    int minMyPrice = MyPrice.MIN_MY_PRICE;
    User user = new User("username", "password", "email", Role.USER);
    ProductCreateRequest product = createProduct("title1", "image1", "link1", 10000);
    Product product1 = productRepository.save(ProductCreateRequest.toEntity(product, user));
    MyPriceUpdateRequest request = new MyPriceUpdateRequest(minMyPrice - 1);
    // when
    assertThatThrownBy(() -> productService.updateMyPrice(product1.getId(), request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(("유효하지 않은 관심 가격입니다. 최소 " + minMyPrice + " 원 이상으로 설정해 주세요."));
  }

  private ProductCreateRequest createProduct(
      String title, String image, String link, Integer lprice) {
    return new ProductCreateRequest(title, image, link, lprice);
  }
}
