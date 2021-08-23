package myfuture.gifticonhub.domain.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.item.model.*;
import myfuture.gifticonhub.domain.item.service.FileService;
import myfuture.gifticonhub.domain.item.service.ItemService;
import myfuture.gifticonhub.domain.member.model.Member;
import myfuture.gifticonhub.domain.member.service.MemberService;
import myfuture.gifticonhub.global.session.Login;
import myfuture.gifticonhub.global.session.SessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/items")
public class ItemController {
    @Autowired
    private FileService fileService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ItemService itemService;

    @ModelAttribute("itemCategories")
    public ItemCategory[] itemCategories() {
        return ItemCategory.values();
    }

    @GetMapping
    public String item(Model model, @Login SessionDto loginSession) {
        List<Item> items = itemService.findItems(loginSession.getId());
        List<ItemViewDto> itemViewDtos = ItemViewDto.toItemViewDtos(items);
        model.addAttribute("itemViewDtos", itemViewDtos);
        return "item/main";
    }

    @GetMapping(value = "/new")
    public String addItemForm(Model model) {
        model.addAttribute("itemRegisterDto", new ItemRegisterDto());
        return "item/addItem";
    }

    @PostMapping(value = "/new")
    public String addItem(@Validated @ModelAttribute ItemRegisterDto itemRegisterDto, BindingResult bindingResult,
                          @Login SessionDto loginSession) throws IOException {
        log.info("itemRegisterDto ={}", itemRegisterDto);
        if (loginSession == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "item/addItem";
        }
        UploadFile uploadFile = fileService.storeFile(itemRegisterDto.getAttachFile(), loginSession.getId());
        Optional<Member> member = memberService.findOne(loginSession.getId());
        Item item = itemRegisterDto.toEntity(member.get(), uploadFile);
        Item registerdItem = itemService.register(item);
        return "redirect:/items";
    }

    @GetMapping(value = "/image/{fileName}")
    @ResponseBody
    public Resource showImage(@PathVariable String fileName, @Login SessionDto loginSession) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(fileName, loginSession.getId()));
    }

    @GetMapping(value = "/{itemId}")
    public String editForm(@PathVariable Long itemId, @Login SessionDto loginSession, Model model) {
        Optional<Item> item = itemService.findOne(itemId, loginSession.getId());
        ItemViewDto itemViewDto = new ItemViewDto(item.get());
        model.addAttribute("itemViewDto", itemViewDto);
        return "item/item";
    }
}
