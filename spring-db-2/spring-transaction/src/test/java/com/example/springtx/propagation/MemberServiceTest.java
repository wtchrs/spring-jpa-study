package com.example.springtx.propagation;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    /**
     * {@code memberService}    {@link Transactional}: Not exist <br/>
     * {@code memberRepository} {@link Transactional}: Exist <br/>
     * {@code logRepository}    {@link Transactional}: Exist <br/>
     */
    @Test
    void outerTransactionOff_success() {
        String username = "outerTransactionOff_success";
        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * {@code memberService}    {@link Transactional}: Not exist <br/>
     * {@code memberRepository} {@link Transactional}: Exist <br/>
     * {@code logRepository}    {@link Transactional}: Exist (RuntimeException occurred) <br/>
     */
    @Test
    void outerTransactionOff_fail() {
        String username = "artificial_exception_outerTransactionOff_fail";
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isExactlyInstanceOf(RuntimeException.class);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * {@code memberService}    {@link Transactional}: Exist <br/>
     * {@code memberRepository} {@link Transactional}: Not exist <br/>
     * {@code logRepository}    {@link Transactional}: Not exist <br/>
     */
    @Test
    void singleTx() {
        String username = "singleTx";
        memberService.joinWithOuterTxOnlyV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * {@code memberService}    {@link Transactional}: Exist <br/>
     * {@code memberRepository} {@link Transactional}: Exist <br/>
     * {@code logRepository}    {@link Transactional}: Exist <br/>
     */
    @Test
    void outerTxOn_success() {
        String username = "outerTxOn_success";
        memberService.joinWithTxV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * {@code memberService}    {@link Transactional}: Exist <br/>
     * {@code memberRepository} {@link Transactional}: Exist <br/>
     * {@code logRepository}    {@link Transactional}: Exist (RuntimeException occurred) <br/>
     */
    @Test
    void outerTransactionOn_fail() {
        String username = "artificial_exception_outerTransactionOn_fail";
        assertThatThrownBy(() -> memberService.joinWithTxV1(username))
                .isExactlyInstanceOf(RuntimeException.class);

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * {@code memberService}    {@link Transactional}: Exist <br/>
     * {@code memberRepository} {@link Transactional}: Exist <br/>
     * {@code logRepository}    {@link Transactional}: Exist (RuntimeException occurred) <br/>
     */
    @Test
    void recoverException_fail() {
        String username = "artificial_exception_recoverException_fail";
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isExactlyInstanceOf(UnexpectedRollbackException.class);

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * {@code memberService}    {@link Transactional}: Exist <br/>
     * {@code memberRepository} {@link Transactional}: Exist <br/>
     * {@code logRepository}    {@link Transactional}: Exist(REQUIRES_NEW) (RuntimeException occurred) <br/>
     */
    @Test
    void recoverException_success() {
        String username = "artificial_exception_recoverException_success";
        memberService.joinWithRequiresNewV2(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }

}
