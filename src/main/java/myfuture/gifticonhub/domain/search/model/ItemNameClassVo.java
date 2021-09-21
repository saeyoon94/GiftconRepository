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
        if (itemRegisterDto.getItemName().isBlank()) {
            itemRegisterDto.setItemName(this.getItemName());
        }
        if (itemRegisterDto.getItemCategory() == null) {
            itemRegisterDto.setItemCategory(this.getItemCategory());
        }
    }
}
