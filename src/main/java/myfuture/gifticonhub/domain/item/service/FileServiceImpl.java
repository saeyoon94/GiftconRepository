package myfuture.gifticonhub.domain.item.service;


import myfuture.gifticonhub.domain.item.model.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public String getFullPath(String fileName, Long loginMemberId) {
        //경로가 없는 경우 생성하고, 완전한 경로 반환
        String dirPath = fileDir + "/" + loginMemberId;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dirPath + "/" + fileName;
    }

    @Override
    public UploadFile storeFile(MultipartFile multipartFile, Long loginMemberId) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFilename, loginMemberId)));

        return new UploadFile(originalFilename, storeFilename);
    }

    public String createStoreFilename(String originalFilename) {
        String ext = extractedExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractedExt(String originalFilename) {
        int position = originalFilename.lastIndexOf(".");
        return originalFilename.substring(position + 1);
    }


}
