package com.example.servletstudy.web.springmvc.v1;

import com.example.servletstudy.domain.member.Member;
import com.example.servletstudy.domain.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
public class SpringMemberSaveControllerV1 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView saveProcess(HttpServletRequest request) {
        log.info("SpringMemberSaveControllerV1.saveProcess");

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member savedMember = memberRepository.save(new Member(username, age));
        log.info("savedMember = {}", savedMember);

        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("savedMember", savedMember);

        return mv;
    }
}
