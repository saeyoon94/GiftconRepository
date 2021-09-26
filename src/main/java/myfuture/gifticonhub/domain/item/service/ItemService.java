package myfuture.gifticonhub.domain.item.service;

import myfuture.gifticonhub.domain.item.model.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    public Item register(Item item);
    public Optional<Item> findOne(Long itemId, Long memberId);

    public List<Item> findItems(Long memberId);

    public Item modifyItem(Long memberId, Long itemId, ItemEditDto itemEditDto) throws IOException;

    public List<Item> updateItemStatus(List<Item> items, LocalDate now);

    public Item applyUsedStatus(Item item, Boolean isUsed);

    public void init();

    public ItemRegisterDto autoFillRegisterFormByImg(ItemRegisterDto itemRegisterDto, MultipartFile multipartFile);

    public void deleteItem(Long memberId, Item item) throws IllegalAccessException;
}
