package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String getMember(@PathVariable("id") Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        return findMember.orElseThrow().getUsername();
    }

    @GetMapping("/members/domain-class-converter/{id}")
    public String getMemberWithDomainClassConverter(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> pagingAndSortList(@PageableDefault(size = 5) Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::of);
    }

//    @PostConstruct
    public void init() {
        for (int i = 1; i <= 100; i++) {
            memberRepository.save(new Member("user" + i, 20));
        }
    }
}
