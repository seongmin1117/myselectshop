package com.sparta.myselectshop.naver.presentation;

import com.sparta.myselectshop.naver.application.NaverService;
import com.sparta.myselectshop.naver.application.dto.NaverSearchItems;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/naver")
public class NaverController {
    private final NaverService naverService;

    @GetMapping("/search")
    public NaverSearchItems searchItems(@RequestParam String query) {
        return naverService.searchItems(query);
    }
}
