package myfuture.gifticonhub.domain.member.controller;

import myfuture.gifticonhub.domain.member.model.LoginDto;
import myfuture.gifticonhub.domain.member.model.Member;
import myfuture.gifticonhub.domain.member.model.MemberDto;
import myfuture.gifticonhub.domain.member.service.LoginService;
import myfuture.gifticonhub.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.global.session.Login;
import myfuture.gifticonhub.global.session.SessionConst;
import myfuture.gifticonhub.global.session.SessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @Autowired
    private final LoginService loginService;

    @GetMapping(value = "/")
    public String home(@Login SessionDto loginSession, HttpServletRequest request) {

        //return "item/addItem";

        if (loginSession == null) {
            return "redirect:/login";
        }
        return "redirect:/items";


    }

    @GetMapping(value = "/login")
    public String loginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "home/index";
    }

    @PostMapping(value = "/login")
    public String login(@Validated @ModelAttribute LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "home/index";
        }
        Optional<Member> member = loginService.login(loginDto);
        if (member.isEmpty()) {
            bindingResult.reject("incorrectUserNameOrPassword");
            return "home/index";
        }
        log.info("login success! userId ={}", loginDto.getUserId());

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, new SessionDto(member.get().getId(), member.get().getUserId()));

        return "redirect:/";
    }

    @PostMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/join")
    public String joinForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "home/join";
    }

    @PostMapping(value = "/join")
    public String join(@Validated @ModelAttribute MemberDto memberDto, BindingResult bindingResult) {
        validateMember(memberDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult.getFieldError());
            //chooseTheMostImportantError(bindingResult);
            return "home/join";
        }

        Member member = memberDto.toEntity();
        memberService.join(member);
        return "redirect:/login";
    }

/*
    private void chooseTheMostImportantError(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError allError : allErrors) {
            if (allError instanceof FieldError) {
                FieldError fieldError = (FieldError) allError;
                System.out.println("fieldError.getField() = " + fieldError.getField() + " // " + fieldError.getCodes()[fieldError.getCodes().length-1]);
                if (fieldError.getField() == "userName" && fieldError.getCodes()[fieldError.getCodes().length - 1] != "NotBlank") {
                    bindingResult.getAllErrors().remove(fieldError);
                }

            } else {
                System.out.println("allError.getCodes() = " + allError.getCodes()[allError.getCodes().length-1]);
            }
        }
    }
*/

    private void validateMember(MemberDto memberDto, BindingResult bindingResult) {
        //패스워드와 확인용 패스워드가 다른 경우
        if (memberDto.isNotMatchedPassword()) {
            bindingResult.reject("unmatchedPassword");
        }
        //생년월일 검증
        if (memberDto.validateDate() == MemberDto.DateValidationStatus.TOO_EARLY) {
            bindingResult.reject("tooEarlyBirthDay");
        } else if (memberDto.validateDate() == MemberDto.DateValidationStatus.FUTURE) {
            bindingResult.reject("futureBirthDay");
        }
    }
}
