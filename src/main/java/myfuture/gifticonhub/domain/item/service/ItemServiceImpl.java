package myfuture.gifticonhub.domain.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.item.model.Item;
import myfuture.gifticonhub.domain.item.model.ItemEditDto;
import myfuture.gifticonhub.domain.item.model.ItemViewDto;
import myfuture.gifticonhub.domain.item.model.UploadFile;
import myfuture.gifticonhub.domain.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final FileService fileService;

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

    @Override
    public Item modifyItem(Long memberId, Long itemId, ItemEditDto itemEditDto) throws IOException {
        Optional<Item> item = findOne(itemId, memberId);
        //파일 업로드하지 않은 경우
        if (itemEditDto.getAttachFile().isEmpty()) {
            item.ifPresent(itemEditDto::editEntity);
        }
        //파일 업로드한 경우
        else {
            UploadFile uploadFile = fileService.storeFile(itemEditDto.getAttachFile(), memberId);
            Item itemFromDB = item.orElseThrow(() -> new NoSuchObjectException("There is no such Item in Database."));
            String storedFileName = itemFromDB.getUploadFile().getStoredFileName();
            boolean isDeleted = fileService.deleteFile(storedFileName, memberId);
            itemEditDto.editEntity(itemFromDB, uploadFile);

            if (isDeleted) {
                log.info("File Delete Success!!, fileName={}", storedFileName);
            } else {
                log.info("File Delete Fails!!, fileName={}", storedFileName);
            }
        }

        return item.get();
    }
}
