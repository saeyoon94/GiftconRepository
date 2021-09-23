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
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
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
     *  현재 날짜와 만기일을 비교하여 자동으로 아이템상태 변환하는 로직 구현 -> 가능하면 캐시도 적용해보자.
     *  main페이지에서 전체 조회한 데이터는 ehcashe를 통해 저장해서 사용하다가 수정이나 등록이 발생한 경우
     *  캐시를 리셋하는 방식으로 할까 했는데... 너무 조회 빈도가 낮을 것 같아서 여기에는 사용 안하는걸로...
     *  앞으로 검색 기능등이 개발되면 고려해보자.
     *  -> 검색 기능 개발해서 캐시도 사용중
     *
     *  대신 아이템상태 변경하는건 스프링 배치로 구현해보자.
     *  MemberService도 단위테스트 작성
     *  view단 더 수정
     *  현재 기프티콘 등록 페이지로 리다이렉트될 때 다른 필드의 값은 폼에 채워지지만 파일은 없어지는 현상이 발생중
     *  -> 이미지 등록과 나머지 화면을 분리하여 해결. 그런데 itemRegisterDto의 uploadFile필드가 post요청시 안 넘어오는걸 확인.
     *  -> 찾아본 결과 html의 input value에는 문자열로 나타낼 수 있는 데이터만 전송할 수 있었기에 이 객체의 값이
     *  -> 백엔드로 다시 돌아오지 않아 null이 된 것임. 이를 해결하기 위해 프론트에서 작업을 할 수도 있었지만,
     *  -> 커스텀 포메터를 적용하여 view로 넘어갈 때 객체를 문자열로 바꾸었다가 다시 서버로 넘어올 때 객체로 전환되도록 하여 해결.
     *
     *  기타 정리할 것 : addFlashAttribute, MultiparfFile 저장하면 달라져서 못 읽는거, 객체 받았을 때 인풋으로 multipartFile이
     *  없어지는 현상
     *
     *  view단 수정했는데 submit 버튼 눌러도 post요청이 가지 않는 문제 발생
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

    //기프티콘 이미지 등록 폼
    @GetMapping(value = "/new")
    public String addItemImageForm(Model model, @ModelAttribute FileDto fileDto) {
        if (fileDto == null) {
            model.addAttribute("fileDto", new FileDto());
        }
        return "item/addImage";
    }

    //기프티콘 이미지 등록
    @PostMapping(value = "/new")
    public String addItemImage(Model model, @Validated @ModelAttribute FileDto fileDto, BindingResult bindingResult,
                                @Login SessionDto loginSession, RedirectAttributes redirectAttributes) throws IOException {
        if (fileDto.getAttachFile().isEmpty()) {
            bindingResult.rejectValue("attachFile", "NotEmptyFile");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}",bindingResult);
            return "item/addImage";
        }
        MultipartFile attachFile = fileDto.getAttachFile();
        log.info("Uploaded FileName = {}", attachFile.getOriginalFilename());

        ItemRegisterDto autoFilledItemRegisterDto = itemService.autoFillRegisterFormByImg(new ItemRegisterDto(), attachFile);

        UploadFile uploadFile = fileService.storeFile(attachFile, loginSession.getId());
        log.info("Uploaded FileName = {}", attachFile.getOriginalFilename());
        redirectAttributes.addFlashAttribute("itemRegisterDto", autoFilledItemRegisterDto);
        redirectAttributes.addFlashAttribute(uploadFile);
        return "redirect:/items/new/additionalInfo";
    }

    //기프티콘 등록 폼
    @GetMapping(value = "/new/additionalInfo")
    public String addItemForm(Model model, @ModelAttribute ItemRegisterDto itemRegisterDto,
                              @ModelAttribute UploadFile uploadFile, HttpServletRequest request) {
        itemRegisterDto.setUploadFile(uploadFile);
        log.info("itemRegisterDto={}", itemRegisterDto);
        if (itemRegisterDto == null) {
            model.addAttribute("itemRegisterDto", new ItemRegisterDto());
        }

        return "item/addItem";
    }

    //기프티콘 등록 처리
    @PostMapping(value = "/new/additionalInfo")
    public String addItem(@Validated @ModelAttribute ItemRegisterDto itemRegisterDto, BindingResult bindingResult,
                          @Login SessionDto loginSession) {
        log.info("itemRegisterDto ={}", itemRegisterDto);
        if (loginSession == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "item/addItem";
        }

        Optional<Member> member = memberService.findOne(loginSession.getId());

        Item item = itemRegisterDto.toEntity(member.get());
        log.info("item={}", item);
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
            log.info("Item(id={}) Item from DB", itemId);
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

    //상세페이지에서 기프트콘 사용여부 체크

    /**
     try1 : 아이템 상태를 수정하는 비지니스 로직은 엔티티에 직접 작성해서 컨트롤러에서 직접 사용
     -> Entity가 수정되었으나 update 쿼리가 나가지 않음
     try2 : 수정 비지니스 로직을 컨트롤러가 아닌 서비스 계층에서 호출
     -> 트랜잭션 범위 밖에서 호출된거라 commit이 안 들어간다고 가정하고 @Transactional이 붙은 서비스계층에서 호출
     -> 여전히 쿼리가 나가지 않음
     try3 : 이미 예전에 commit된 엔티티를 세션에 저장해 두었다가 꺼내 쓰는 것이므로 준영속 상태가 된 것임을 가정
     -> em.persist 호출 결과 detached entity passed to persist 에러 발생. 가정이 맞았음을 확신하고 em.merge로 해결
     -> 그러나 업데이트 쿼리만 나갈 수 있도록 최적화하기 위해 세션에 저장해두고 쓰는건데 em.merge시 update 전에 select쿼리가 나가고 있음.
     try4 : em.merge시 준영속 상태의 entity가 DB에 있는 엔티티인지 여부를 확인하기위해 select쿼리가 먼저 나가게 되어있음
     -> 이를 막기 위해 JPQL로 직접 update쿼리 날려서 해결
     */
    @PostMapping(value = "/{itemId}")
    public String checkIfUsed(@RequestParam(value = "isUsed", defaultValue = "false") Boolean isUsed, HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws IllegalAccessException {
        log.info("isUsed={}", isUsed);
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("isUsed? session=null!");
        }
        Item item = (Item) session.getAttribute(SessionConst.TEMP_MODEL);
        if (item == null) {
            throw new IllegalAccessException("No Item in Session!");
        }
        Item appliedItem = itemService.applyUsedStatus(item, isUsed);
        redirectAttributes.addFlashAttribute("item", appliedItem);

        return "redirect:/items/{itemId}";
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
            log.info("editForm from Session : itemEditDto={}", itemEditDto);
        } else { //SessionConst.TEMP_MODEL 세션값이 없는 경우(/{itemId}에서 바로 넘어오지 않은 경우)
            Item itemFromDB = itemService.findOne(itemId, loginSession.getId()).get();
            ItemEditDto itemEditDto = new ItemEditDto(itemFromDB);
            model.addAttribute("itemEditDto", itemEditDto);
            log.info("editForm From DB");
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
/**
    //아이템 등록 시 자동 채우기
    @PostMapping(value = "/new/autofill")
    public String autoFill(@Validated @ModelAttribute ItemRegisterDto itemRegisterDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "item/addItem";
        }

        ItemRegisterDto autoFilledItemRegisterDto = itemService.autoFillRegisterFormByImg(itemRegisterDto);
        redirectAttributes.addFlashAttribute("itemRegisterDto", autoFilledItemRegisterDto);
        return "redirect:/items/new";
    }
    */
}
