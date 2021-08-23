package myfuture.gifticonhub.domain.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import myfuture.gifticonhub.domain.member.model.Member;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemViewDto {

    private String itemName;

    private String brandName;

    private LocalDate expirationDate;

    private ItemCategory itemCategory;

    private ItemStatus itemStatus;

    private Long price;

    private String imageURI;

    public ItemViewDto(Item item) {
        this.itemName = item.getItemName();
        this.brandName = item.getBrandName();
        this.expirationDate = item.getExpirationDate();
        this.itemCategory = item.getItemCategory();
        this.itemStatus = item.getItemStatus();
        this.price = item.getPrice();
        this.imageURI = item.getUploadFile().getStoredFileName();
    }

    public static List<ItemViewDto> toItemViewDtos(List<Item> items) {
        List<ItemViewDto> itemViewDtos = new ArrayList<>();
        for (Item item : items) {
            itemViewDtos.add(new ItemViewDto(item));
        }
        return itemViewDtos;
    }
}