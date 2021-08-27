package myfuture.gifticonhub.domain.item.service;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.item.model.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
/**
 * 아래 value로 파일경로를 받는게 스프링에 의존적이다보니 단위테스트가 불가하여 final을 붙이고 @RequiredArgsConstructor를 붙여 사용
 * 그렇게 직접 생성자로 값을 주입하여 단위테스트는 진행하였는데 @Value에 값이 주입되는 시점이 Bean의 초기화시점보다 느려
 * 이 빈이 초기화되지 못하여 컨테이너가 올라오지 못함. 그래서 fileDir을 생성자로 주입받을 수도 있고 아닐수도 있도록
 * 기본 생성자와 fileDir이 포함된 생성자를 모두 정의
 */
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

    @Override
    public boolean deleteFile(String storeFileName, Long loginMemberId) {
        File file = new File(getFullPath(storeFileName, loginMemberId));
        if (file.exists()) {
            log.info("Deleting file : name={}", storeFileName);
            return file.delete();
        }
        log.info("File delete fails : name={}, No Such Files", storeFileName);
        return false;
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
