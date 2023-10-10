package com.example.jdbcstudy.service;

import com.example.jdbcstudy.domain.Member;
import com.example.jdbcstudy.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Transaction - using Template callback pattern(TransactionTemplate)
 */
@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate txTemplate;

    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager txManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(txManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int amount) {
        txTemplate.executeWithoutResult((status) -> accountTransferLogic(fromId, toId, amount));
    }

    private void accountTransferLogic(String fromId, String toId, int amount) {
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
