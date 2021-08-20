package myfuture.gifticonhub.domain.item.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myfuture.gifticonhub.domain.member.model.Member;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    /*todo :
    이건 등록용 DTO이고, 등록폼에 price넣기. 그리고 조회용 DTO를 따로 만들어야겠다.
     */


    private String itemName;

    private String brandName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    //날짜가 String으로 오기 때문에 이걸 붙여야 LocalDate 등으로 변경 가능하다.
    //mm이 아니라 MM 주의 -> mm은 분을 의미한다.
    private LocalDate expirationDate;

    private ItemCategory itemCategory;

    private String serialNumber;

    private Long price;

    @NotNull
    private MultipartFile attachFile;

    public Item toEntity(Member member, UploadFile uploadFile) {
        return new Item(member, itemName, brandName, LocalDate.now(), expirationDate, price, itemCategory, serialNumber, uploadFile);

    }
}
