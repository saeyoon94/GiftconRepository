package myfuture.gifticonhub.domain.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemEditDto {

    private String itemName;

    private String brandName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    //날짜가 String으로 오기 때문에 이걸 붙여야 LocalDate 등으로 변경 가능하다.
    //mm이 아니라 MM 주의 -> mm은 분을 의미한다.
    private LocalDate expirationDate;

    private ItemCategory itemCategory;

    private String serialNumber;

    @NumberFormat(pattern = "###,###")
    private Long price;

    private MultipartFile attachFile;

    @NotNull
    private String imageURI;

    public ItemEditDto(Item item) {
        this.itemName = item.getItemName();
        this.brandName = item.getBrandName();
        this.expirationDate = item.getExpirationDate();
        this.itemCategory = item.getItemCategory();
        this.serialNumber = item.getSerialNumber();
        this.price = item.getPrice();
        this.imageURI = item.getUploadFile().getStoredFileName();
    }

    //파일을 수정해서 업로드한 경우 사용
    public Item editEntity(Item item, UploadFile uploadFile) {
        item.setItemName(this.getItemName());
        item.setBrandName(this.getBrandName());
        item.setExpirationDate(this.getExpirationDate());
        item.setItemCategory(this.getItemCategory());
        item.setSerialNumber(this.getSerialNumber());
        item.setPrice(this.getPrice());
        item.setLastModified(LocalDateTime.now());
        item.setUploadFile(uploadFile);
        return item;
    }

    //파일을 업로드하지 않았을 때 사용
    public Item editEntity(Item item) {
        item.setItemName(this.getItemName());
        item.setBrandName(this.getBrandName());
        item.setExpirationDate(this.getExpirationDate());
        item.setItemCategory(this.getItemCategory());
        item.setSerialNumber(this.getSerialNumber());
        item.setPrice(this.getPrice());
        item.setLastModified(LocalDateTime.now());
        return item;
    }
}
