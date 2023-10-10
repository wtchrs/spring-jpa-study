package com.example.jdbcstudy.exception.translation;

import com.example.jdbcstudy.connection.ConnectionConst;
import com.example.jdbcstudy.domain.Member;
import com.example.jdbcstudy.repository.exception.MyDbException;
import com.example.jdbcstudy.repository.exception.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.example.jdbcstudy.connection.ConnectionConst.*;

public class ExceptionTranslationV1Test {

    static Repository repository;
    static Service service;

    @BeforeAll
    static void initialize() {
        DataSource ds = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(ds);
        service = new Service(repository);
    }

    @Test
    void duplicateExceptionTest() {
        service.create("memberId");

    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service {

        private final Repository repository;

        public void create(String memberId) {
            try {
                log.info("member_id = {}", memberId);
                repository.save(new Member(memberId, 0));
            } catch (MyDuplicateKeyException e) {
                log.info("Duplicated member_id, rescue");
                String newId = generateNewId(memberId);
                log.info("generated new member_id = {}", newId);
                repository.save(new Member(newId, 0));
            }
        }

        private String generateNewId(String id) {
            return id + new Random().nextInt(10000);
        }

    }

    @RequiredArgsConstructor
    static class Repository {

        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member (member_id, money) values (?, ?)";
            Connection con = null;
            PreparedStatement pstmt = null;

            try {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            } catch (SQLException e) {
                // h2 db error code 23505: duplicate key
                if (e.getErrorCode() == 23505) {
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e);
            } finally {
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(con);
            }
        }

    }
}
