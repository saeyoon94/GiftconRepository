package myfuture.gifticonhub.domain.search.service;

import myfuture.gifticonhub.domain.search.model.TextClassification;
import myfuture.gifticonhub.domain.search.repository.TextClassificationEsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EsRepositoryTest {

    @Autowired
    TextClassificationEsRepository repository;

    @Test
    void searchTest() {
        TextClassification textClassification = repository.findFirstByText("황금올리브치킨 반반+ 콜라 1.25").get();

        System.out.println("textClassification = " + textClassification);
        System.out.println("textClassification.getText = " + textClassification.getText());
        System.out.println("textClassification.getId = " + textClassification.getId());
        System.out.println("textClassification.getType = " + textClassification.getType());
        System.out.println("textClassification.getCategory = " + textClassification.getCategory());

    }
}
