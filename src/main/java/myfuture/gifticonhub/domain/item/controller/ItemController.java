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
     *  main페이지에서 전체 조회한 데이터는 ehcashe를 통해 저장해서 사용하다가 수정이나 등록이 발생한 경우
     *  캐시를 리셋하는 방식으로 할까 했는데... 너무 조회 빈도가 낮을 것 같아서 여기에는 사용 안하는걸로...
     *  앞으로 검색 기능등이 개발되면 고려해보자.
     *
     *  대신 아이템상태 변경하는건 스프링 배치로 구현해보자.
     *  MemberService도 단위테스트 작성
     *  만기일 수정하면 상태도 갱신되도록 작성 + 사용완료 체크도 할 수 있게 수정
     *  view단 더 수정

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
    public String itemDetails(HttpServletRequest request, @PathVariable Long itemId, @Login SessionDto loginSession,
                              Model model, @ModelAttribute Item itemThroughRedirection) {

        //일반적인 경우
        Item item;
        if (itemThroughRedirection.getId() == null) {  //itemThroughRedirection=null로 했더니 모든 필드가 null인 객체로 초기화되어 분기를 안탐
            log.info("Item(id={}) View Normal Case", itemId);
            item = itemService.findOne(itemId, loginSession.getId()).get();
        }
        //상품 수정 후 리다이렉션으로 item이 넘어오는 경우
        else {
            log.info("Item(id={}) View From Redirect Item={}", itemId, itemThroughRedirection);
            item = itemThroughRedirection;
        }
        ItemViewDto itemViewDto = new ItemViewDto(item);
        model.addAttribute("itemViewDto", itemViewDto);

        //아이템 수정 뷰에서 사용할 수 있도록 item객체를 세션에 저장
        //(이 방법을 redirectAttributes보다 먼저 생각해내서 이렇게 적용한건데, 둘이 동작 방식이 비슷한가?
        //redirect상황이 아닐때도 사용이 가능한지는 모르겠지만 이 방식으로 두자.
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
        Item item = itemService.modifyItem(loginSession.getId(), itemId, itemEditDto);
        redirectAttributes.addFlashAttribute("item", item);
        return "redirect:/items/{itemId}";

    }
}
