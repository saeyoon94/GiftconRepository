package myfuture.gifticonhub.domain.item.service;

import myfuture.gifticonhub.domain.item.model.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public String getFullPath(String fileName, Long loginMemberId);
    public UploadFile storeFile(MultipartFile multipartFile, Long loginMemberId) throws IOException;
    public boolean deleteFile(String storedFileName, Long loginMemberId);

}
