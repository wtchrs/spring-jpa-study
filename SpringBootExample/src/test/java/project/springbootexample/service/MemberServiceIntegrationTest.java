package project.springbootexample.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.springbootexample.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    private final MemberService memberService;

    @Autowired
    public MemberServiceIntegrationTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @Test
    void join() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        Long resultId = memberService.join(member);

        // then
        Member result = memberService.findOne(resultId).get();
        assertThat(result.getName()).isEqualTo(member.getName());
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
        assertThat(e.getMessage()).isEqualTo("Already exist duplicate name");
    }
}