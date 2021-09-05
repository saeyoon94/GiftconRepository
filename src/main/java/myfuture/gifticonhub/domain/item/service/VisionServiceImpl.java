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
    public String getTextDetection(String imageLocation) {
        Resource imageResource = resourceLoader.getResource(imageLocation);
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(
                imageResource, Feature.Type.DOCUMENT_TEXT_DETECTION);
        return response.getTextAnnotationsList().get(0).getDescription();
    }
}
