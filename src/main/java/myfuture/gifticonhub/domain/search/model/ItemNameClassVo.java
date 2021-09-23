package myfuture.gifticonhub.domain.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import myfuture.gifticonhub.domain.item.model.ItemCategory;
import myfuture.gifticonhub.domain.item.model.ItemRegisterDto;

@Getter
@RequiredArgsConstructor
@ToString
public class ItemNameClassVo extends ClassVo {
    private final String itemName;
    private final ItemCategory itemCategory;

    @Override
    public void autoFill(ItemRegisterDto itemRegisterDto) {
        itemRegisterDto.setItemName(this.getItemName());

        //brandName이 ItemName보다 더 신뢰할 만 하다고 판단하여 여기는 null인 경우만 삽입
        if (itemRegisterDto.getItemCategory() == null) {
            itemRegisterDto.setItemCategory(this.getItemCategory());
        }
    }
}
