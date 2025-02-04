package com.sparta.myselectshop.product.infrastructure;

import com.sparta.myselectshop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
