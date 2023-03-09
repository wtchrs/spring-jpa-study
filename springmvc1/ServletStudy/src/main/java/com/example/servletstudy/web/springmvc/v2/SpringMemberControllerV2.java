package com.example.servletstudy.web.springmvc.v2;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        log.info("SpringMemberControllerV2.newForm");
        return new ModelAndView("new-form");
    }

    @RequestMapping("/save")
    public ModelAndView saveProcess(HttpServletRequest request) {
        log.info("SpringMemberControllerV2.saveProcess");

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);

        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("savedMember", savedMember);

        return mv;
    }

    @RequestMapping
    public ModelAndView list() {
        log.info("SpringMemberControllerV2.list");

        List<Member> members = memberRepository.findAll();
        ModelAndView mv = new ModelAndView("memberList");
        mv.addObject("members", members);

        return mv;
    }
}
