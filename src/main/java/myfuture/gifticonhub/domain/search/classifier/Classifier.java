package myfuture.gifticonhub.domain.search.classifier;

import myfuture.gifticonhub.domain.search.model.ClassVo;

public interface Classifier {
    public boolean canClassify(String text);

    public ClassVo classify(String text);
}
