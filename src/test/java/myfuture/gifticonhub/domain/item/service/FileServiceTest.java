package myfuture.gifticonhub.domain.item.service;

import myfuture.gifticonhub.domain.item.model.UploadFile;
import myfuture.gifticonhub.domain.member.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {
    FileService fileService = new FileServiceImpl("C:/Users/mutal/Desktop/project/GiftconRepository/src/main/resources/static/file");

    @Test
    @DisplayName("multipartFile=null인 경우 저장되지 않는다.")
    void storeFileFail() throws IOException {
        //given
        Member member = new Member();
        member.setId(1L);
        MockMultipartFile multipartFile = new MockMultipartFile("testFile", "Test1.png", "image/png", (InputStream) null);

        //when
        UploadFile uploadFile = fileService.storeFile(multipartFile, member.getId());

        //then
        Assertions.assertThat(uploadFile).isNull();
    }

    @Test
    @DisplayName("정상적으로 저장되는 경우")
    void storeFileSuccess() throws IOException {
        //given
        Member member = new Member();
        member.setId(1L);
        MockMultipartFile multipartFile = new MockMultipartFile("testFile", "Test1.png", "image/png", new byte[1]);

        //when
        UploadFile uploadFile = fileService.storeFile(multipartFile, member.getId());

        //then
        Assertions.assertThat(uploadFile.getUploadFileName()).isEqualTo("Test1.png");
        Assertions.assertThat(uploadFile.getStoredFileName().substring(uploadFile.getStoredFileName().length() - 3)).isEqualTo("png");
    }

    @Test
    @DisplayName("파일이 없어서 삭제가 안되는 경우")
    void deleteFileFail() {
        //given
        Member member = new Member();
        member.setId(1L);

        //when
        boolean result = fileService.deleteFile("12345.png", member.getId());

        //then
        Assertions.assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("파일이 삭제되는 정상 흐름")
    void deleteFileSuccess() throws IOException {
        //given
        Member member = new Member();
        member.setId(1L);
        MockMultipartFile multipartFile = new MockMultipartFile("testFile", "Test1.png", "image/png", new byte[1]);
        UploadFile uploadFile = fileService.storeFile(multipartFile, member.getId());

        //when
        boolean result = fileService.deleteFile(uploadFile.getStoredFileName(), member.getId());

        //then
        Assertions.assertThat(result).isEqualTo(true);
    }
}