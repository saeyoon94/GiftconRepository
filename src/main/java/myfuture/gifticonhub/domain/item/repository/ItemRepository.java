package myfuture.gifticonhub.domain.item.repository;

import myfuture.gifticonhub.domain.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);
    Optional<Item> findById(Long id, Long memberId) ;
    List<Item> findByMemberId(Long memberId);
    List<Item> findAll();
    Item rePersist(Item item);
}
