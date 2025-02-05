package com.sparta.myselectshop.product.presentation;

import com.sparta.myselectshop.auth.security.UserDetailsImpl;
import com.sparta.myselectshop.product.application.ProductService;
import com.sparta.myselectshop.product.application.dto.MyPriceUpdateRequest;
import com.sparta.myselectshop.product.application.dto.ProductCreateRequest;
import com.sparta.myselectshop.product.application.dto.ProductListResponse;
import com.sparta.myselectshop.product.application.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
  private final ProductService productService;

  @PostMapping()
  public ResponseEntity<ProductResponse> createProduct(
      @Valid @RequestBody ProductCreateRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ProductResponse response = productService.create(request, userDetails.user());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/admin")
  public ResponseEntity<ProductListResponse> getProductsByAdmin(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc) {
    ProductListResponse response = productService.getProductsByAdmin(page, size, sortBy, isAsc);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping()
  public ResponseEntity<ProductListResponse> getProductsByUser(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ProductListResponse response =
        productService.getProductsByUser(userDetails.user(), page, size, sortBy, isAsc);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PutMapping("{id}")
  public ResponseEntity<Void> updateMyPrice(
      @PathVariable Long id, @RequestBody MyPriceUpdateRequest request) {
    productService.updateMyPrice(id, request);
    return ResponseEntity.ok().build();
  }
}
