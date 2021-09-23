package myfuture.gifticonhub.domain.item.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class FileDto {

    private MultipartFile attachFile;
}
