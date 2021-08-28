package myfuture.gifticonhub.domain.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import myfuture.gifticonhub.domain.member.model.Member;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemViewDto {

    private Long id;

    private String itemName;

    private String brandName;

    private LocalDate expirationDate;

    private ItemCategory itemCategory;

    private ItemStatus itemStatus;

    private Boolean isUsed;

    @NumberFormat(pattern = "###,###")
    private Long price;

    private String imageURI;

    public ItemViewDto(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.brandName = item.getBrandName();
        this.expirationDate = item.getExpirationDate();
        this.itemCategory = item.getItemCategory();
        this.itemStatus = item.getItemStatus();
        this.isUsed = item.getItemStatus() == ItemStatus.Already_Used;
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
