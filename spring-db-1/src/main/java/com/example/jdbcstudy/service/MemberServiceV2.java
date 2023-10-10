package com.example.jdbcstudy.service;

import com.example.jdbcstudy.domain.Member;
import com.example.jdbcstudy.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource ds;

    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int amount) throws SQLException {
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            accountTransferLogic(con, fromId, toId, amount);

            con.commit();
        } catch (Exception e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    private void accountTransferLogic(Connection con, String fromId, String toId, int amount) {
        Member from = memberRepository.findById(con, fromId);
        Member to = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, from.getMoney() - amount);
        validation(to); // an artificial exception.
        memberRepository.update(con, toId, to.getMoney() + amount);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("Exception occurs during transfer.");
        }
    }

}
