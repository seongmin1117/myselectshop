package com.sparta.myselectshop.product.application;

import com.sparta.myselectshop.naver.application.NaverService;
import com.sparta.myselectshop.naver.application.dto.NaverSearchItem;
import com.sparta.myselectshop.naver.application.dto.NaverSearchItems;
import com.sparta.myselectshop.product.domain.Product;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j(topic = "ProductScheduler")
@Service
@RequiredArgsConstructor
public class ProductScheduler {
  private final ProductService productService;
  private final NaverService naverService;

  @Scheduled(cron = "0 0 1 * * *")
  public void updatePrice() throws InterruptedException {
    log.info("상품 최저가 업데이트 실행");
    List<Product> products = productService.findAll();
    for (Product product : products) {
      // 1초에 한 상품씩 조회 (Naver API 제한)
      TimeUnit.SECONDS.sleep(1);
      updateBySearch(product);
    }
  }

  private void updateBySearch(Product product) {
    NaverSearchItems searchResults = naverService.searchItems(product.getTitle());
    if (searchResults.items().isEmpty()) {
      log.warn("[가격 미갱신] '{}' 상품의 네이버 검색 결과 없음", product.getTitle());
    }
    NaverSearchItem bestMatchItem = searchResults.items().get(0);
    productService.updateBySearch(product.getId(), bestMatchItem.lprice());
    log.info(
        "[가격 업데이트] 상품 '{}': {}원 → {}원",
        product.getTitle(),
        product.getLprice(),
        bestMatchItem.lprice());
  }
}
