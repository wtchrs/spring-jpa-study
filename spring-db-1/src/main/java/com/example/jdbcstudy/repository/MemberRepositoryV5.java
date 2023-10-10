package com.example.jdbcstudy.repository;

import com.example.jdbcstudy.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Using JdbcTemplate. It can replace all duplicated lines of code that use JDBC with just few lines of code.
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {

    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource ds) {
        this.template = new JdbcTemplate(ds);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (member_id, money) values (?, ?);";
        template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public Member findById(String id) {
        String sql = "select member_id, money from member where member_id = ?;";
        return template.queryForObject(
                sql, (rs, rowNum) -> new Member(rs.getString("member_id"), rs.getInt("money")), id);
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql, money, memberId);
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }

}
