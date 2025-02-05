package com.sparta.myselectshop.product.application.dto;

import com.sparta.myselectshop.product.domain.Product;
import org.springframework.data.domain.Page;

public record ProductListResponse(Page<ProductResponse> products) {

  public static ProductListResponse toDtoList(Page<Product> products) {
    return new ProductListResponse(products.map(ProductResponse::toDto));
  }
}
