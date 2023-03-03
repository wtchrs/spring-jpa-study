package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();
    }

    @Test
    public void basicTest() {
        Member member = new Member("testMember", 10);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember.getUsername()).isEqualTo("testMember");
        assertThat(findMember.getAge()).isEqualTo(10);

        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).extracting("username")
                           .containsExactly("member1", "member2", "member3", "member4", "testMember");

        List<Member> result2 = memberRepository.findByUsername("testMember");
        assertThat(result2).containsExactly(member);
    }

    @Test
    public void searchTest() {
        List<MemberTeamDto> result = memberRepository.search(new MemberSearchCondition(null, "TeamB", 15, 45));
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }

        assertThat(result).hasSize(2);
        assertThat(result).extracting("username").containsExactly("member3", "member4");
        assertThat(result).extracting("teamName").containsExactly("TeamB", "TeamB");
    }

    @Test
    public void searchPageSimpleTest() {
        MemberSearchCondition condition = new MemberSearchCondition();
        Pageable pageable = PageRequest.of(0, 3);

        Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageable);
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }

        assertThat(result).hasSize(3);
        assertThat(result).extracting("username").containsExactly("member1", "member2", "member3");
        assertThat(result).extracting("teamName").containsExactly("TeamA", "TeamA", "TeamB");
    }

    @Test
    public void querydslPredicateExecutorTest() {
        Iterable<Member> result = memberRepository.findAll(team.name.eq("TeamA"));

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }
    }
}