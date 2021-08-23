package myfuture.gifticonhub.domain.item.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ItemStatus {
    Available("사용가능"), Expired("사용기간만료"), Already_Used("사용완료");

    private final String value;
}
