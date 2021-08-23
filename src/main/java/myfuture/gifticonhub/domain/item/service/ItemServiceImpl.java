package myfuture.gifticonhub.domain.item.service;

import lombok.RequiredArgsConstructor;
import myfuture.gifticonhub.domain.item.model.Item;
import myfuture.gifticonhub.domain.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    @Autowired
    private final ItemRepository itemRepository;

    @Override
    public Item register(Item item) {
        Item savedItem = itemRepository.save(item);
        return savedItem;
    }

    @Override
    public Optional<Item> findOne(Long itemId, Long memberId) {
        return itemRepository.findById(itemId, memberId);
    }

    @Override
    public List<Item> findItems(Long memberId) {
        return itemRepository.findByMemberId(memberId);
    }
}
