package com.example.jdbcstudy.repository;

import com.example.jdbcstudy.domain.Member;

public interface MemberRepository {

    Member save(Member member);

    Member findById(String id);

    void update(String memberId, int money);

    void delete(String memberId);

}
