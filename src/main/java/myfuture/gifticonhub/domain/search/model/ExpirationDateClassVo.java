package myfuture.gifticonhub.domain.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import myfuture.gifticonhub.domain.item.model.ItemRegisterDto;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@ToString
public class ExpirationDateClassVo extends ClassVo {
    private final LocalDate expirationDate;

    @Override
    public void autoFill(ItemRegisterDto itemRegisterDto) {
        if (itemRegisterDto.getExpirationDate() == null) {
            itemRegisterDto.setExpirationDate(this.getExpirationDate());
        }
    }
}
