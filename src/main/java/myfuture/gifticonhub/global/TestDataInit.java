package myfuture.gifticonhub.global;

import lombok.RequiredArgsConstructor;
import myfuture.gifticonhub.domain.item.model.Item;
import myfuture.gifticonhub.domain.item.model.ItemCategory;
import myfuture.gifticonhub.domain.item.model.UploadFile;
import myfuture.gifticonhub.domain.item.service.ItemService;
import myfuture.gifticonhub.domain.member.model.Member;
import myfuture.gifticonhub.domain.member.model.Sex;
import myfuture.gifticonhub.domain.member.service.MemberService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

//테스트데이터 생성용
@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberService memberService;
    private final ItemService itemService;

    @PostConstruct
    public void init() {
        Member member = new Member("saeyoon94", "youngsook50", "김세윤",
                LocalDate.of(1994, 3, 23), Sex.Male,
                "mutal2gc@naver.com", "01025430363");
        Member foundMember = memberService.join(member);

        Item item1 = new Item(foundMember, "황금올리브치킨반반+콜라1.25", "BBQ", LocalDate.of(2021, 8, 10),
                LocalDate.of(2021, 10, 3), ItemCategory.Chicken_Pizza_Burger, "111630278042",
                new UploadFile("bbq.jpeg", "443724c1-d610-4850-9141-aeaf68429ab6.jpeg"));

        Item item2 = new Item(foundMember, "달콤한 디저트 세트(7 레이어 가나슈 케이크 + 아메리카노 Tall 1잔)", "스타벅스",
                LocalDate.of(2021, 8, 15),
                LocalDate.of(2021, 10, 11), ItemCategory.CAFE_Bakery, "961830613068",
                new UploadFile("starbucks.jpeg", "b124e64f-3374-45e7-b34f-0a5fcb2f1ef6.jpeg"));

        Item item3 = new Item(foundMember, "파인트 아이스크림", "베스킨라빈스",
                LocalDate.of(2021, 8, 20),
                LocalDate.of(2021, 9, 8), ItemCategory.IceCream, "92000702004663",
                new UploadFile("br.png", "b136e64f-3274-45h7-b44f-0a5fcn2f1ef9.png"));

        itemService.register(item1);
        itemService.register(item2);
        itemService.register(item3);
    }


}
