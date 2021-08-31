package myfuture.gifticonhub.domain.item.service;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisionServiceImpl implements VisionService {

    @Autowired
    private final CloudVisionTemplate cloudVisionTemplate;
    @Autowired
    private final ResourceLoader resourceLoader;

    @Override
    public String getTextDetection() {
        Resource imageResource = resourceLoader.getResource("file:C:/Users/mutal/Desktop/project/GiftconRepository/src/main/resources/static/file/1/443724c1-d610-4850-9141-aeaf68429ab6.jpeg");
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(
                imageResource, Feature.Type.DOCUMENT_TEXT_DETECTION);
        return response.getTextAnnotationsList().toString();
    }
}
