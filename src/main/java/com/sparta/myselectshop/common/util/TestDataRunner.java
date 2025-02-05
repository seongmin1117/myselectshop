package com.sparta.myselectshop.common.util;

import static com.sparta.myselectshop.product.domain.vo.MyPrice.MIN_MY_PRICE;

import com.sparta.myselectshop.naver.application.NaverService;
import com.sparta.myselectshop.naver.application.dto.NaverSearchItem;
import com.sparta.myselectshop.naver.application.dto.NaverSearchItems;
import com.sparta.myselectshop.product.domain.Product;
import com.sparta.myselectshop.product.infrastructure.ProductRepository;
import com.sparta.myselectshop.user.application.UserService;
import com.sparta.myselectshop.user.domain.Role;
import com.sparta.myselectshop.user.domain.User;
import com.sparta.myselectshop.user.infrastructure.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    UserService userService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    NaverService naverService;

    @Override
    public void run(ApplicationArguments args) {
        // 테스트 User 생성
        User testUser = new User("Robbie", passwordEncoder.encode("1234"), "robbie@sparta.com", Role.USER);
        testUser = userRepository.save(testUser);

        // 테스트 User 의 관심상품 등록
        // 검색어 당 관심상품 10개 등록
        createTestData(testUser, "신발");
        createTestData(testUser, "과자");
        createTestData(testUser, "키보드");
        createTestData(testUser, "휴지");
        createTestData(testUser, "휴대폰");
        createTestData(testUser, "앨범");
        createTestData(testUser, "헤드폰");
        createTestData(testUser, "이어폰");
        createTestData(testUser, "노트북");
        createTestData(testUser, "무선 이어폰");
        createTestData(testUser, "모니터");
    }

    private void createTestData(User user, String searchWord) {
        // 네이버 쇼핑 API 통해 상품 검색
        NaverSearchItems items = naverService.searchItems(searchWord);

        List<Product> productList = new ArrayList<>();

        for (NaverSearchItem item: items.items()) {
            Product product = Product.builder()
                .user(user)
                .title(item.title())
                .link(item.link())
                .image(item.image())
                .lprice(item.lprice())
                .build();

            // 희망 최저가 랜덤값 생성
            // 최저 (100원) ~ 최대 (상품의 현재 최저가 + 10000원)
            int myPrice = getRandomNumber(MIN_MY_PRICE, item.lprice() + 10000);
            product.updateMyPrice(myPrice);

            productList.add(product);
        }

        productRepository.saveAll(productList);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}