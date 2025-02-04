package com.sparta.myselectshop.product.infrastructure;

import com.sparta.myselectshop.product.domain.Product;
import com.sparta.myselectshop.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findAllByUser(User user);
}
