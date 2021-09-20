package myfuture.gifticonhub.domain.search.config;

import lombok.RequiredArgsConstructor;
import myfuture.gifticonhub.domain.search.classifier.*;
import myfuture.gifticonhub.domain.search.repository.TextClassificationEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class TextClassificationConfig {

    @Autowired
    private final ClassifierRegistry classifierRegistry;

    @Autowired
    private final TextClassificationEsRepository textClassificationEsRepository;

    @PostConstruct
    public void init() {
        this.addClassifiers(classifierRegistry);
    }

    public void addClassifiers(ClassifierRegistry classifierRegistry) {

        classifierRegistry.addClassifier(new SerialNumberClassifier());
        classifierRegistry.addClassifier(new ExpirationDateClassifier());
        classifierRegistry.addClassifier(this.esClassifier());
    }

    //elastic search의 검색 서비스(TextClassificationService)를 DI 받기 위해 bean으로 등록해야 하는 classifier들이 있어
    //여기서 Bean으로 등록, 나머지는 위에서 등록할 때 new로 생성
    @Bean
    public Classifier esClassifier() {
        return new EsClassifier(textClassificationEsRepository);
    }




}
