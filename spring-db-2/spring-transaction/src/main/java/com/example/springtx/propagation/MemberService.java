package com.example.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final LogRepository logRepository;

    public void joinV1(String username) {
        log.info("[Calling MemberRepository.save()]");
        memberRepository.save(new Member(username));

        log.info("[Calling LogRepository.save()]");
        logRepository.save(new Log(username));

        log.info("[End joinV1()]");
    }

    @Transactional
    public void joinWithOuterTxOnlyV1(String username) {
        log.info("[Calling MemberRepository.save()]");
        memberRepository.saveWithoutTx(new Member(username));

        log.info("[Calling LogRepository.save()]");
        logRepository.saveWithoutTx(new Log(username));

        log.info("[End joinV1()]");
    }

    @Transactional
    public void joinWithTxV1(String username) {
        log.info("[Calling MemberRepository.save()]");
        memberRepository.save(new Member(username));

        log.info("[Calling LogRepository.save()]");
        logRepository.save(new Log(username));

        log.info("[End joinV1()]");
    }

    @Transactional
    public void joinV2(String username) {
        log.info("[Calling MemberRepository.save()]");
        memberRepository.save(new Member(username));

        log.info("[Calling LogRepository.save()]");
        Log logMessage = new Log(username);
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
            log.info("[Fail saving log. logMessage = {}]", logMessage);
        }

        log.info("[End joinV2()]");
    }

    @Transactional
    public void joinWithRequiresNewV2(String username) {
        log.info("[Calling MemberRepository.save()]");
        memberRepository.save(new Member(username));

        log.info("[Calling LogRepository.save()]");
        Log logMessage = new Log(username);
        try {
            logRepository.saveWithRequiresNew(logMessage);
        } catch (RuntimeException e) {
            log.info("[Fail saving log. logMessage = {}]", logMessage);
        }

        log.info("[End joinV2()]");
    }

}
