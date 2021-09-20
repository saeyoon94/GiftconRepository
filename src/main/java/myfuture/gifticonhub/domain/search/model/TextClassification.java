package myfuture.gifticonhub.domain.search.model;

import lombok.Getter;
import lombok.Setter;
import myfuture.gifticonhub.domain.item.model.ItemCategory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "text_classifier")
@Getter
@Setter
public class TextClassification {

    @Id
    private String id;

    private ItemCategory category;
    private String type;
    private String text;
}
