package myfuture.gifticonhub.domain.search.service;

import myfuture.gifticonhub.domain.item.service.VisionService;
import myfuture.gifticonhub.domain.search.model.ClassVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClassifierServiceTest {

    @Autowired
    ClassifierService classifierService;

    @Autowired
    VisionService visionService;

    @Test
    void classifyTest() {

        String textDetection = visionService.getTextDetection("file:C:/Users/mutal/Desktop/project/GiftconRepository/src/main/resources/static/file/1/443724c1-d610-4850-9141-aeaf68429ab6.jpeg");
        String[] detections = textDetection.split("\n");
        for (String detection : detections) {
            ClassVo classVo = classifierService.classify(detection);
            System.out.println("detection = " + detection);
            if (classVo != null) {
                System.out.println("classVo = " + classVo.toString());
            }

        }
    }

}
