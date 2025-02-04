package com.sparta.myselectshop.product.domain;

import com.sparta.myselectshop.common.BaseTimeEntity;
import com.sparta.myselectshop.product.domain.vo.MyPrice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  private String link;

  @Column(nullable = false)
  private Integer lprice;

  @Embedded
  private MyPrice myPrice;

  @Builder
  private Product(String title, String image, String link, Integer lprice, Integer myPrice) {
    this.title = title;
    this.image = image;
    this.link = link;
    this.lprice = lprice;
    this.myPrice = new MyPrice(myPrice);
  }

  public void updateMyPrice(Integer value) {
    this.myPrice = myPrice.update(value);
  }
}
