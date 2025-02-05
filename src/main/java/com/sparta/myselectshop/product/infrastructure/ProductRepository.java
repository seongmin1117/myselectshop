package com.sparta.myselectshop.product.infrastructure;

import com.sparta.myselectshop.product.domain.Product;
import com.sparta.myselectshop.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findAllByUser(User user, Pageable pageable);
}
