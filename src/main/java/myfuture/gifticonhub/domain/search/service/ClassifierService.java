package myfuture.gifticonhub.domain.search.service;


import myfuture.gifticonhub.domain.search.model.ClassVo;

public interface ClassifierService {
    public ClassVo classify(String text);

}
