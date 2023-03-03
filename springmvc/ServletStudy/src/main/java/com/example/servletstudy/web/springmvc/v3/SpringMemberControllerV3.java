package com.example.servletstudy.web.springmvc.v3;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @GetMapping("/new-form")
    public String newForm() {
        log.info("SpringMemberControllerV3.newForm");
        return "new-form";
    }

    @PostMapping("/save")
    public String saveProcess(Model model, @RequestParam("username") String username, @RequestParam("age") int age) {
        log.info("SpringMemberControllerV3.saveProcess");

        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);

        model.addAttribute("savedMember", savedMember);

        return "save-result";
    }

    @GetMapping
    public String list(Model model) {
        log.info("SpringMemberControllerV3.list");

        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);

        return "memberList";
    }
}
