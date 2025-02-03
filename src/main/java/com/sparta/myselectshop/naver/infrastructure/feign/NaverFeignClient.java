package com.sparta.myselectshop.naver.infrastructure.feign;

import com.sparta.myselectshop.naver.application.dto.NaverSearchItems;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NaverFeignClient", url = "https://openapi.naver.com")
public interface NaverFeignClient {

  @GetMapping("/v1/search/shop.json")
  NaverSearchItems searchItems(
      @RequestParam("query") String query,
      @RequestParam("display") int display,
      @RequestHeader("X-Naver-Client-Id") String clientId,
      @RequestHeader("X-Naver-Client-Secret") String clientSecret);
}
