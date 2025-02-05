package com.sparta.myselectshop.product.application;

import com.sparta.myselectshop.product.application.dto.MyPriceUpdateRequest;
import com.sparta.myselectshop.product.application.dto.ProductCreateRequest;
import com.sparta.myselectshop.product.application.dto.ProductListResponse;
import com.sparta.myselectshop.product.application.dto.ProductResponse;
import com.sparta.myselectshop.product.domain.Product;
import com.sparta.myselectshop.product.infrastructure.ProductRepository;
import com.sparta.myselectshop.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;

  public ProductResponse create(ProductCreateRequest request, User user) {
    Product product = productRepository.save(ProductCreateRequest.toEntity(request, user));
    return ProductResponse.toDto(product);
  }

  @Transactional(readOnly = true)
  public List<Product> findAll() {
    return productRepository.findAll();
  }

  @Transactional(readOnly = true)
  public ProductListResponse getProductsByAdmin(int page, int size, String sortBy, boolean isAsc) {
    Pageable pageable = getPageable(page, size, sortBy, isAsc);
    Page<Product> products = productRepository.findAll(pageable);
    return ProductListResponse.toDtoList(products);
  }

  @Transactional(readOnly = true)
  public ProductListResponse getProductsByUser(
      User user, int page, int size, String sortBy, boolean isAsc) {
    Pageable pageable = getPageable(page, size, sortBy, isAsc);
    Page<Product> products = productRepository.findAllByUser(user, pageable);
    return ProductListResponse.toDtoList(products);
  }

  public void updateBySearch(Long id, Integer lprice) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
    product.updateLprice(lprice);
  }

  public void updateMyPrice(Long id, MyPriceUpdateRequest request) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
    product.updateMyPrice(request.myPrice());
  }

  private Pageable getPageable(int page, int size, String sortBy, boolean isAsc) {
    Direction direction = isAsc ? Direction.ASC : Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    return PageRequest.of(page, size, sort);
  }
}
