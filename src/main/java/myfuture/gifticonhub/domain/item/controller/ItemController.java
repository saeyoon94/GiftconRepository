package myfuture.gifticonhub.domain.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.item.model.*;
import myfuture.gifticonhub.domain.item.service.FileService;
import myfuture.gifticonhub.domain.item.service.ItemService;
import myfuture.gifticonhub.domain.member.model.Member;
import myfuture.gifticonhub.domain.member.service.MemberService;
import myfuture.gifticonhub.global.session.Login;
import myfuture.gifticonhub.global.session.SessionConst;
import myfuture.gifticonhub.global.session.SessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    /**
     *  todo : 상품수정 view와 기능 구현하고 ItemSatus CSS 잘 작동하는지 확인.
     *  excludepatterns 자세히 알아보기(/new에서 /*로 적용되어버리는거같음)
     *  현재 날짜와 만기일을 비교하여 자동으로 아이템상태 변환하는 로직 구현 -> 가능하면 캐시도 적용해보자.
     *  상품 수정한 다음 redirect할 때 itemViewDto가 그대로 같이 넘어가서 DB조회 할 필요 없도록 해보자.

     */

    @ModelAttribute("itemCategories")
    public ItemCategory[] itemCategories() {
        return ItemCategory.values();
    }

    //메인화면 view
    @GetMapping
    public String item(Model model, @Login SessionDto loginSession) {
        List<Item> items = itemService.findItems(loginSession.getId());
        List<ItemViewDto> itemViewDtos = ItemViewDto.toItemViewDtos(items);
        model.addAttribute("itemViewDtos", itemViewDtos);
        return "item/main";
    }

    //기프티콘 등록 폼
    @GetMapping(value = "/new")
    public String addItemForm(Model model) {
        model.addAttribute("itemRegisterDto", new ItemRegisterDto());
        return "item/addItem";
    }

    //기프티콘 등록 처리
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

    //뷰에서 이미지 보여주기
    @GetMapping(value = "/image/{fileName}")
    @ResponseBody
    public Resource showImage(@PathVariable String fileName, @Login SessionDto loginSession) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(fileName, loginSession.getId()));
    }

    //메인화면에서 특정 아이템 선택했을 때 보여주는 상세 페이지
    @GetMapping(value = "/{itemId}")
    public String itemDetails(HttpServletRequest request, @PathVariable Long itemId, @Login SessionDto loginSession, Model model) {
        Item item = itemService.findOne(itemId, loginSession.getId()).get();
        ItemViewDto itemViewDto = new ItemViewDto(item);
        model.addAttribute("itemViewDto", itemViewDto);

        //아이템 수정 뷰에서 사용할 수 있도록 item객체를 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.TEMP_MODEL, item);
        return "item/item";
    }

    //상세 아이템 수정 뷰
    @GetMapping(value = "/{itemId}/edit")
    public String editForm(HttpServletRequest request, @PathVariable Long itemId, @Login SessionDto loginSession, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("editForm, session=null");
            return "redirect:/login";
        }
        Item item = (Item) session.getAttribute(SessionConst.TEMP_MODEL);
        if (item != null) {  //SessionConst.TEMP_MODEL 세션값이 있는 경우(/{itemId}에서 바로 넘어온 경우)
            ItemEditDto itemEditDto = new ItemEditDto(item);
            model.addAttribute("itemEditDto", itemEditDto);
            log.info("editForm, itemEditDto={}", itemEditDto);
        } else { //SessionConst.TEMP_MODEL 세션값이 없는 경우(/{itemId}에서 바로 넘어오지 않은 경우)
            Item itemFromDB = itemService.findOne(itemId, loginSession.getId()).get();
            ItemEditDto itemEditDto = new ItemEditDto(itemFromDB);
            model.addAttribute("itemEditDto", itemEditDto);
            log.info("editForm, no itemEditDto");
        }
        return "item/edit";
    }

    //아이템 수정
    @PostMapping(value = "/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute ItemEditDto itemEditDto, BindingResult bindingResult,
                       @Login SessionDto loginSession, RedirectAttributes redirectAttributes) throws IOException {
        log.info("itemEditDto={}", itemEditDto);
        if (loginSession == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            log.info("errors={}",bindingResult);
            return "item/edit";
        }
        ItemViewDto itemViewDto = itemService.modifyItem(loginSession.getId(), itemId, itemEditDto);
        return "redirect:/items/{itemId}";

    }
}
