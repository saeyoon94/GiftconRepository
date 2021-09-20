package myfuture.gifticonhub.domain.search.service;

import myfuture.gifticonhub.domain.search.classifier.Classifier;
import myfuture.gifticonhub.domain.search.classifier.ClassifierRegistry;
import myfuture.gifticonhub.domain.search.model.ClassVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultClassifierService implements ClassifierService, ClassifierRegistry {
    private static List<Classifier> classifiers = new ArrayList<>();

    @Override
    public void addClassifier(Classifier classifier) {
        classifiers.add(classifier);
    }

    @Override
    public ClassVo classify(String text) {
        for (Classifier classifier : classifiers) {
            if (classifier.canClassify(text)) {
                return classifier.classify(text);
            }
        }
        return null;
    }
}
