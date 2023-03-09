package com.example.servletstudy.web.springmvc.v1;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
public class SpringMemberListControllerV1 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView list() {
        log.info("SpringMemberListControllerV1.list");

        List<Member> members = memberRepository.findAll();
        ModelAndView mv = new ModelAndView("memberList");
        mv.addObject("members", members);

        return mv;
    }
}
