package com.example.jdbcstudy.repository;

import com.example.jdbcstudy.connection.ConnectionConst;
import com.example.jdbcstudy.connection.DBConnectionUtil;
import com.example.jdbcstudy.domain.Member;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.NoSuchElementException;

import static com.example.jdbcstudy.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    static MemberRepositoryV1 repository;

    @BeforeAll
    static void initialize() {
//        DataSource ds = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        config.setPoolName("Test Pool");

        DataSource ds = new HikariDataSource(config);
        repository = new MemberRepositoryV1(ds);
    }

    @Test
    void crud() {
        Member member = new Member("member 1", 10000);

        // save
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        assertThat(findMember).isEqualTo(member);

        // update: money 10000 -> 20000
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Not found: member_id: member 1");
    }
}