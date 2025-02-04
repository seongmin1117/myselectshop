package com.sparta.myselectshop.product.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MyPrice {

  public static final int MIN_MY_PRICE = 100;

  @Column(name = "myPrice", nullable = false)
  private Integer value;

  public MyPrice(Integer value) {
    this.value = value;
  }

  public MyPrice update(Integer value) {
    validate(value);
    return new MyPrice(value);
  }

  private void validate(Integer value) {
    if (value < MIN_MY_PRICE) {
      throw new IllegalArgumentException(
          "유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.");
    }
  }
}
