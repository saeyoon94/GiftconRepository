package myfuture.gifticonhub.global;

import lombok.RequiredArgsConstructor;
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

    @PostConstruct
    public void init() {
        Member member = new Member("saeyoon94", "youngsook50", "김세윤",
                LocalDate.of(1994, 3, 23), Sex.Male,
                "mutal2gc@naver.com", "01025430363");
        memberService.join(member);
    }


}
