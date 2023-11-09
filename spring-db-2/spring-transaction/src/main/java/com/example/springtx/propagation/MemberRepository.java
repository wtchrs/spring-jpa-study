package com.example.springtx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    @Transactional
    public void save(Member member) {
        log.info("[Save Member] {}", member);
        em.persist(member);
    }

    public void saveWithoutTx(Member member) {
        log.info("[Save Member] {}", member);
        em.persist(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> find(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultStream()
                .findAny();
    }

}
