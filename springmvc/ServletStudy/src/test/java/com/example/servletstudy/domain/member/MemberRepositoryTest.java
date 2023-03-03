package com.example.servletstudy.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }


    @Test
    void save() {
        Member member = new Member("hello", 20);

        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId());

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void findAll() {
        Member member1 = new Member("hello1", 20);
        Member member2 = new Member("hello2", 30);
        Member member3 = new Member("hello3", 40);
        Member member4 = new Member("hello4", 50);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> result = memberRepository.findAll();

        assertThat(result).hasSize(4);
        assertThat(result).containsExactly(member1, member2, member3, member4);
    }
}