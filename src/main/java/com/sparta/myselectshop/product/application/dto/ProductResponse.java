package com.sparta.myselectshop.product.application.dto;

import com.sparta.myselectshop.product.domain.Product;

public record ProductResponse(
    Long productId, String title, String image, String link, Integer lprice, Integer myPrice) {

  public static ProductResponse toDto(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getTitle(),
        product.getImage(),
        product.getLink(),
        product.getLprice(),
        product.getMyPrice().getValue());
  }
}
