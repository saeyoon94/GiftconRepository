package myfuture.gifticonhub.domain.item.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor //임베디드타입도 엔티티와 마찬가지로 기본생성자가 필수
public class UploadFile {
    private String uploadFileName;
    private String storedFileName;

}
