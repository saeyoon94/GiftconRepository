package myfuture.gifticonhub.domain.item.service;

import myfuture.gifticonhub.domain.item.model.Item;
import myfuture.gifticonhub.domain.item.model.ItemEditDto;
import myfuture.gifticonhub.domain.item.model.ItemStatus;
import myfuture.gifticonhub.domain.item.model.UploadFile;
import myfuture.gifticonhub.domain.item.repository.ItemRepository;
import myfuture.gifticonhub.domain.item.repository.JpaItemRepository;
import myfuture.gifticonhub.domain.member.model.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    FileService fileService = new FileServiceImpl("C:/Users/mutal/Desktop/project/GiftconRepository/src/main/resources/static/file");
    ItemService itemService = new ItemServiceImpl(itemRepository, fileService);

    @Test
    @DisplayName("아이템 저장")
    void register() {
        //given
        Item item = new Item();
        item.setId(1L);
        Mockito.when(itemRepository.save(item)).thenReturn(item);

        //when
        Item registerdItem = itemService.register(item);

        //then
        Assertions.assertThat(registerdItem).isEqualTo(item);


    }

    @Test
    @DisplayName("아이템 조회")
    void findOne() {
        //given
        Member member = new Member();
        member.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setMember(member);
        Mockito.when(itemRepository.findById(item.getId(), item.getMember().getId())).thenReturn(Optional.of(item));

        //when
        Optional<Item> foundItem = itemService.findOne(item.getId(), member.getId());

        //then
        Assertions.assertThat(foundItem.get()).isEqualTo(item);
    }

    @Test
    @DisplayName("아이템 모두 조회")
    void findItems() {
        //given
        Member member = new Member();
        member.setId(1L);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setMember(member);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setMember(member);
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        Mockito.when(itemRepository.findByMemberId(member.getId())).thenReturn(items);

        //when
        List<Item> foundItems = itemService.findItems(member.getId());

        //then
        Assertions.assertThat(foundItems.size()).isEqualTo(items.size());
    }

    @Test
    @DisplayName("아이템 수정")
    void modifyItem() throws IOException {
        //given
        ItemEditDto itemEditDto = new ItemEditDto();
        itemEditDto.setItemName("아메리카노");
        itemEditDto.setBrandName("스타벅스");
        itemEditDto.setAttachFile(new MockMultipartFile("testFile", "tobe.png", "image/png", new byte[1]));
        Item item = new Item();
        item.setId(1L);
        Member member = new Member();
        member.setId(1L);
        item.setMember(member);
        UploadFile uploadFile = fileService.storeFile(new MockMultipartFile("testFile2", "asis.png", "image/png", new byte[2]), member.getId());
        item.setUploadFile(uploadFile);
        Mockito.when(itemRepository.findById(item.getId(), item.getMember().getId())).thenReturn(Optional.of(item));

        //when
        Item modifyItem = itemService.modifyItem(item.getMember().getId(), item.getId(), itemEditDto);

        //then
        Assertions.assertThat(modifyItem.getId()).isEqualTo(item.getId());
        Assertions.assertThat(modifyItem.getItemName()).isEqualTo(itemEditDto.getItemName());
        Assertions.assertThat(modifyItem.getUploadFile().getUploadFileName()).isEqualTo(itemEditDto.getAttachFile().getOriginalFilename());
    }

    @Test
    @DisplayName("아이템 상태 업데이트")
    void updateItemStatus() {
        //given
        LocalDate now = LocalDate.of(2021, 8, 28);

        //->expired로 바뀌어야 함
        Item item1 = new Item();
        item1.setId(1L);
        item1.setExpirationDate(LocalDate.of(2021, 8, 27));
        item1.setItemStatus(ItemStatus.Available);

        //->impending으로 바뀌어야 함
        Item item2 = new Item();
        item2.setId(2L);
        item2.setExpirationDate(LocalDate.of(2021, 8, 30));
        item2.setItemStatus(ItemStatus.Available);

        //->변화가 없어야 함
        Item item3 = new Item();
        item3.setId(3L);
        item3.setExpirationDate(LocalDate.of(2021, 8, 31));
        item3.setItemStatus(ItemStatus.Available);

        //->변화가 없어야 함
        Item item4 = new Item();
        item4.setId(4L);
        item4.setExpirationDate(LocalDate.of(2021, 8, 13));
        item4.setItemStatus(ItemStatus.Already_Used);

        //->impending으로 바뀌어야 함
        Item item5 = new Item();
        item5.setId(5L);
        item5.setExpirationDate(LocalDate.of(2021, 8, 28));
        item5.setItemStatus(ItemStatus.Available);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);

        //when
        List<Item> resultItemList = itemService.updateItemStatus(items, now);

        //then
        Assertions.assertThat(resultItemList.size()).isEqualTo(3); //통합테스트에서는 쿼리가 3개만 나가는지도 확인해봐야 함
        Assertions.assertThat(items.get(0).getItemStatus()).isEqualTo(ItemStatus.Expired);
        Assertions.assertThat(items.get(1).getItemStatus()).isEqualTo(ItemStatus.Impending);
        Assertions.assertThat(items.get(2).getItemStatus()).isEqualTo(ItemStatus.Available);
        Assertions.assertThat(items.get(3).getItemStatus()).isEqualTo(ItemStatus.Already_Used);
        Assertions.assertThat(items.get(4).getItemStatus()).isEqualTo(ItemStatus.Impending);

    }
}