package myfuture.gifticonhub.domain.item.service;

import com.google.cloud.vision.v1.EntityAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VisionServiceTest {
    @Autowired
    VisionService visionService;

    @Test
    void ocr() throws UnsupportedEncodingException {
        String textDetection = visionService.getTextDetection("file:C:/Users/mutal/Desktop/project/GiftconRepository/src/main/resources/static/file/1/443724c1-d610-4850-9141-aeaf68429ab6.jpeg");
        System.out.println(textDetection);


        //for (EntityAnnotation entityAnnotation : textDetection) {
            //System.out.println("entityAnnotation.getDescription = " + entityAnnotation.getDescription());
            //System.out.println("entityAnnotation.getLocale = " + entityAnnotation.getLocale());
            //System.out.println("entityAnnotation.getMid = " + entityAnnotation.getMid());
            //System.out.println("entityAnnotation.getInitializationErrorString = " + entityAnnotation.getInitializationErrorString());
            //System.out.println("entityAnnotation.getDescriptionBytes().toString() = " + entityAnnotation.getDescriptionBytes().toString("utf-8"));

    }
}