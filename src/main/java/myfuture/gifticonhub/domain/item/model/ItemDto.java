package myfuture.gifticonhub.domain.item.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myfuture.gifticonhub.domain.member.model.Member;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {


    private String itemName;

    private String brandName;

    @NotBlank
    private LocalDate expirationDate;

    private ItemCategory itemCategory;

    private String serialNumber;

    @NotNull
    private MultipartFile attachFile;

    public Item toEntity(UploadFile uploadFile) {
        return new Item()
                //todo : 이것 다 작성하고 컨트롤러 작성 후 테스트... session에 Member를 다 담아놓는게 좋을지 지금처럼 하는게 좋을지...
    }
}
