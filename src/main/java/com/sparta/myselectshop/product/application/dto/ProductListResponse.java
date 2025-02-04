package com.sparta.myselectshop.product.application.dto;

import com.sparta.myselectshop.product.domain.Product;
import java.util.List;

public record ProductListResponse(List<ProductResponse> products) {

  public static ProductListResponse toDtoList(List<Product> products) {
    return new ProductListResponse(products.stream().map(ProductResponse::toDto).toList());
  }
}
