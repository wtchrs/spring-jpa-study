package com.example.itemservice.web.member;

import com.example.itemservice.domain.member.Member;
import com.example.itemservice.domain.member.MemberRepository;
import com.example.itemservice.web.member.form.RegisterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/register")
    public String register(@ModelAttribute("member") RegisterForm form) {
        return "members/registerMemberForm";
    }

    @PostMapping("/register")
    public String registerProcess(@Validated @ModelAttribute("member") RegisterForm form, BindingResult bindingResult) {
        if (memberRepository.findByLoginId(form.getLoginId()).isPresent()) {
            bindingResult.rejectValue("loginId", "duplicate");
        }

        if (bindingResult.hasErrors()) {
            return "members/registerMemberForm";
        }

        Member savedMember = memberRepository.save(form.toMember());
        log.info("savedMember = {}", savedMember);

        return "redirect:/members/login";
    }
}
