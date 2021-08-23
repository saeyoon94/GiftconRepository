package myfuture.gifticonhub.domain.item.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {
    CAFE_Bakery("카페/베이커리"), Chicken_Pizza_Burger("치킨/피자/버거"), Restaurant("외식/뷔페"),
    Movie_Culture("영화/문화/생활"), IceCream("아이스크림/빙수"), Conv_Mart("편의점/마트"), Coupon("상품권"), None("기타");

    private final String Value;

}
