package myfuture.gifticonhub.domain.item.model;

import lombok.AllArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
public class UploadFile {
    private String uploadFileName;
    private String storedFileName;

}
