package com.sparta.myselectshop.naver.infrastructure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NaverClientConfig {

    @Value("${naver.client.id}")
    private String id;

    @Value("${naver.client.secret}")
    private String secret;
}
