package myfuture.gifticonhub.domain.item.service;

import myfuture.gifticonhub.domain.item.model.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public UploadFile storeFile(MultipartFile multipartFile, Long loginMemberId) throws IOException;
}
