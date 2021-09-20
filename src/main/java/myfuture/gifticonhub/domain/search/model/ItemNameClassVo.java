package myfuture.gifticonhub.domain.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import myfuture.gifticonhub.domain.item.model.ItemCategory;

@Getter
@RequiredArgsConstructor
@ToString
public class ItemNameClassVo extends ClassVo {
    private final String value;
    private final ItemCategory itemCategory;
}
