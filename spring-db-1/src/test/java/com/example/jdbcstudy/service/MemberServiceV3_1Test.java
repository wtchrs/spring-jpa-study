package com.example.jdbcstudy.service;

import com.example.jdbcstudy.domain.Member;
import com.example.jdbcstudy.repository.MemberRepositoryV2;
import com.example.jdbcstudy.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.example.jdbcstudy.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Transaction - using TransactionManager
 */
@Slf4j
class MemberServiceV3_1Test {

    private static final String MEMBER_A = "memberA";

    private static final String MEMBER_B = "memberB";

    private static final String MEMBER_EX = "ex";

    private static MemberRepositoryV3 memberRepository;

    private static MemberServiceV3_1 memberService;

    @BeforeAll
    static void initialize() {
        DataSource ds = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        PlatformTransactionManager txManager = new DataSourceTransactionManager(ds);

        memberRepository = new MemberRepositoryV3(ds);
        memberService = new MemberServiceV3_1(txManager, memberRepository);
    }

    @AfterEach
    void clean() {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("Normal transfer")
    void accountTransfer() {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        log.info("START TX");
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
        log.info("  END TX");

        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("Exception while transfer")
    void accountTransferEx() {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Exception occurs during transfer.");

        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberEx = memberRepository.findById(memberEx.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000); // rolled back
        assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }

}