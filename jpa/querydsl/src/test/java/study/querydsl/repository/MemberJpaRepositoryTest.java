package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

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
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember.getUsername()).isEqualTo("testMember");
        assertThat(findMember.getAge()).isEqualTo(10);

        List<Member> result1 = memberJpaRepository.findAll();
        assertThat(result1).extracting("username")
                           .containsExactly("member1", "member2", "member3", "member4", "testMember");

        List<Member> result2 = memberJpaRepository.findByUsername("testMember");
        assertThat(result2).containsExactly(member);
    }

    @Test
    public void basicQuerydslTest() {
        Member member = new Member("testMember", 10);
        memberJpaRepository.save(member);

        List<Member> result1 = memberJpaRepository.findAllWithQuerydsl();
        assertThat(result1).extracting("username")
                           .containsExactly("member1", "member2", "member3", "member4", "testMember");

        List<Member> result2 = memberJpaRepository.findWithQuerydslByUsername("testMember");
        assertThat(result2).containsExactly(member);
    }

    @Test
    public void findByBuilderTest() {
        List<MemberTeamDto> result =
            memberJpaRepository.findByBuilder(new MemberSearchCondition(null, "TeamB", 15, 45));
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }

        assertThat(result).hasSize(2);
        assertThat(result).extracting("username").containsExactly("member3", "member4");
        assertThat(result).extracting("teamName").containsExactly("TeamB", "TeamB");

    }

    @Test
    public void findByWhereParamTest() {
        List<MemberTeamDto> result =
            memberJpaRepository.findByWhereParam(new MemberSearchCondition(null, "TeamB", 15, 45));
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }

        assertThat(result).hasSize(2);
        assertThat(result).extracting("username").containsExactly("member3", "member4");
        assertThat(result).extracting("teamName").containsExactly("TeamB", "TeamB");

    }
}