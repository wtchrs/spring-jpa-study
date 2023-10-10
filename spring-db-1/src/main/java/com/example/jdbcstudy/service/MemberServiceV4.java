package com.example.jdbcstudy.service;

import com.example.jdbcstudy.domain.Member;
import com.example.jdbcstudy.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * Using {@link MemberRepository} interface instead of concrete classes.
 */
@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int amount) {
        Member from = memberRepository.findById(fromId);
        Member to = memberRepository.findById(toId);

        memberRepository.update(fromId, from.getMoney() - amount);
        validation(to); // an artificial exception.
        memberRepository.update(toId, to.getMoney() + amount);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("Exception occurs during transfer.");
        }
    }

}
