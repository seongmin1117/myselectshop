package com.sparta.myselectshop.product.application.dto;

import com.sparta.myselectshop.product.domain.Product;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequest(
    @NotNull String title, @NotNull String image, @NotNull String link, @NotNull Integer lprice) {

  public static Product toEntity(ProductCreateRequest request) {
    return Product.builder()
        .title(request.title())
        .image(request.image())
        .link(request.link())
        .lprice(request.lprice())
        .myPrice(0)
        .build();
  }
}
