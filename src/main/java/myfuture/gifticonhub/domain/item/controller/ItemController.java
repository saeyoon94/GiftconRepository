package myfuture.gifticonhub.domain.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.item.model.ItemDto;
import myfuture.gifticonhub.domain.item.model.UploadFile;
import myfuture.gifticonhub.domain.item.service.FileService;
import myfuture.gifticonhub.global.session.Login;
import myfuture.gifticonhub.global.session.SessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/items")
public class ItemController {
    @Autowired
    private FileService fileService;

    @GetMapping
    public String item() {
        return "item/main";
    }

    @GetMapping(value = "/new")
    public String addItemForm(Model model) {
        model.addAttribute("itemDto", new ItemDto());
        return "item/addItem";
    }

    @PostMapping(value = "/new")
    public String addItem(@ModelAttribute ItemDto itemDto, @Login SessionDto loginSession) throws IOException {

        UploadFile uploadFile = fileService.storeFile(itemDto.getAttachFile(), loginSession.getId());

        return "item/addItem";
    }
}
