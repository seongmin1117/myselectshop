package com.sparta.myselectshop.naver.application;

import com.sparta.myselectshop.naver.application.dto.NaverSearchItems;
import com.sparta.myselectshop.naver.infrastructure.config.NaverClientConfig;
import com.sparta.myselectshop.naver.infrastructure.feign.NaverFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverService {

    private final NaverFeignClient naverFeignClient;
    private final NaverClientConfig naverClientConfig;

    public NaverSearchItems searchItems(String query) {
        return naverFeignClient.searchItems(query, 15,
            naverClientConfig.getId(), naverClientConfig.getSecret());
    }
}
