package myfuture.gifticonhub.domain.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import myfuture.gifticonhub.domain.item.model.ItemCategory;
import myfuture.gifticonhub.domain.item.model.ItemRegisterDto;

@Getter
@RequiredArgsConstructor
@ToString
public class BrandNameClassVo extends ClassVo {
    private final String brandName;
    private final ItemCategory itemCategory;

    @Override
    public void autoFill(ItemRegisterDto itemRegisterDto) {
        if (itemRegisterDto.getBrandName().isBlank()) {
            itemRegisterDto.setBrandName(this.getBrandName());
        }

        if (itemRegisterDto.getItemCategory() == null) {
            itemRegisterDto.setItemCategory(this.getItemCategory());
        }
    }
}
