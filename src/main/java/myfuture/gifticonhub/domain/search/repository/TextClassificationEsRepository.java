package myfuture.gifticonhub.domain.search.repository;

import myfuture.gifticonhub.domain.search.model.TextClassification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TextClassificationEsRepository extends ElasticsearchRepository<TextClassification, String> {

    /**
     postman으로 검색할 때는 결과가 잘 나오지만 spring data elasticsearch로 검색하면 결과가 나오지 않는 경우가 있었음.
     삽질하다보니 여기서는 기본적으로 쿼리가 "default_operator":"and"로 나가는 것을 발견. or조건으로 바꿔주기 위해
     @Query 어노테이션 추가.
     그러나 or조건이 붙으니 이번에는 이상한 텍스트들도 검색이 되는 문제가 발행하여 일부 텍스트들을 fallback으로 등록한 다음
     검색 결과에서 fallback에 매징된 결과는 처리하지 않도록 함(EsClassifier
     */
    @Query("{\"match\":{\"text\":{\"query\":\"?0\"}}}")
    @Cacheable(cacheNames = "esRepositoryCache")
    public Optional<TextClassification> findFirstByText(String text);

}
