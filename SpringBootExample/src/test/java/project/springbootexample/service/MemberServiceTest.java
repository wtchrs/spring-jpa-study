package project.springbootexample.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import project.springbootexample.domain.Member;
import project.springbootexample.repository.MemoryMemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    private final MemoryMemberRepository repo = new MemoryMemberRepository();
    private final MemberService memberService = new MemberService(repo);

    @AfterEach
    void afterEach() {
        repo.clear();
    }

    @Test
    void join() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        Long resultId = memberService.join(member);

        // then
        assertThat(repo.findById(resultId).get()).isEqualTo(member);
    }

    @Test
    void joinDuplicate() {
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);

        // then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()) .isEqualTo("Already exist duplicate name");
    }

    @Test
    void findMembers() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring2");

        // when
        memberService.join(member1);
        memberService.join(member2);
        List<Member> results = memberService.findMembers();

        // then
        assertThat(results).contains(member1, member2);
    }

    @Test
    void findOne() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring2");

        // when
        memberService.join(member1);
        memberService.join(member2);

        // then
        assertThat(memberService.findOne(1L).get()).isEqualTo(member1);
        assertThat(memberService.findOne(2L).get()).isEqualTo(member2);
    }
}