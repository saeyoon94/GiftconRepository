package myfuture.gifticonhub.domain.search.classifier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.search.model.BrandNameClassVo;
import myfuture.gifticonhub.domain.search.model.ClassVo;
import myfuture.gifticonhub.domain.search.model.ItemNameClassVo;
import myfuture.gifticonhub.domain.search.model.TextClassification;
import myfuture.gifticonhub.domain.search.repository.TextClassificationEsRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class EsClassifier implements Classifier{

    private final TextClassificationEsRepository textClassificationEsRepository;

    @Override
    public boolean canClassify(String text) {
        log.info("EsClassifier canClassify");
        //fallback이 아닌경우만 해당
        Optional<TextClassification> textClassification = textClassificationEsRepository.findFirstByText(text)
                .filter(t -> !t.getType().equals("fallback"));
        return textClassification.isPresent();
    }

    @Override
    public ClassVo classify(String text) {
        //canClassify로 인해 값이 존재함이 보장
        //위에서 한번 했는데 엘라스틱서치 한번 더 호출해야 하는게 마음에 들지 않는다... 싱글톤이라 필드로 값을 빼서 쓰면
        //문제가 생길 것 같고... 스코프를 바꾸어 쓰기에는 ClassifierService도 스코프를 바꿔야하고 등록도 매번 다시 해야해서...
        //ehcache를 써볼까 -> 캐시를 사용하여 해결(repository에 걸어둠)
        TextClassification textClassification = textClassificationEsRepository.findFirstByText(text).get();
        if (textClassification.getType().equals("menu")) {
            return new ItemNameClassVo(text, textClassification.getCategory());
        } else {
            return new BrandNameClassVo(text, textClassification.getCategory());
        }
    }
}
