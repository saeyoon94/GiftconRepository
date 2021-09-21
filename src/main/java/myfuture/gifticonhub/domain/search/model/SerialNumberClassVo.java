package myfuture.gifticonhub.domain.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import myfuture.gifticonhub.domain.item.model.ItemRegisterDto;

@Getter
@RequiredArgsConstructor
@ToString
public class SerialNumberClassVo extends ClassVo {
    private final String serialNumber;

    @Override
    public void autoFill(ItemRegisterDto itemRegisterDto) {
        if (itemRegisterDto.getSerialNumber().isBlank()) {
            itemRegisterDto.setSerialNumber(this.getSerialNumber());
        }
    }
}
