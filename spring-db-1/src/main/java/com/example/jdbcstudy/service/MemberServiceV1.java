package com.example.jdbcstudy.service;

import com.example.jdbcstudy.domain.Member;
import com.example.jdbcstudy.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int amount) {
        Member from = memberRepository.findById(fromId);
        Member to = memberRepository.findById(toId);

//        if (from.getMoney() < amount) throw new IllegalStateException("Insufficient balance");

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
