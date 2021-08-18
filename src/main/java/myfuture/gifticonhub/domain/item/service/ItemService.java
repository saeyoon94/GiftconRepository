package myfuture.gifticonhub.domain.item.service;

import myfuture.gifticonhub.domain.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    public Item register(Item item);
    public Optional<Item> findOne(Long itemId);

    public List<Item> findItems(Long memberId);
}
