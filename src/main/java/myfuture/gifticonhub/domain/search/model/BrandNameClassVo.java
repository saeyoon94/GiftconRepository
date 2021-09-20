package myfuture.gifticonhub.domain.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import myfuture.gifticonhub.domain.item.model.ItemCategory;

@Getter
@RequiredArgsConstructor
@ToString
public class BrandNameClassVo extends ClassVo {
    private final String brandName;
    private final ItemCategory itemCategory;
}
