package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.datajpa.entity.Member;

import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        System.out.println("MemberRepositoryCustomImpl.findMemberCustom");
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
