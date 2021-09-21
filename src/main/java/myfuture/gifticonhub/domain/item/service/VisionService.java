package myfuture.gifticonhub.domain.item.service;

import org.springframework.core.io.Resource;

public interface VisionService {
    public String[] getTextDetection(String imageLocation);

    public String[] getTextDetection(Resource imageResource);
}
