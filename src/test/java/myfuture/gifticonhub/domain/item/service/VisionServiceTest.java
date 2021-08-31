package myfuture.gifticonhub.domain.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VisionServiceTest {
    @Autowired
    VisionService visionService;

    @Test
    void ocr() {
        String textDetection = visionService.getTextDetection();
        System.out.println("textDetection = " + textDetection);
    }

}