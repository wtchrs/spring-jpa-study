package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 20);
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
    }

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> allMembers = memberRepository.findAll();
        assertThat(allMembers).containsOnly(member1, member2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        count = memberRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void findAsMemberDto() {
        Team team = new Team("Team");
        teamRepository.save(team);

        Member member = new Member("member", 20);
        member.changeTeam(team);
        memberRepository.save(member);

        List<MemberDto> findDtoList = memberRepository.findAsMemberDto();

        assertThat(findDtoList.get(0).getUsername()).isEqualTo("member");
        assertThat(findDtoList.get(0).getTeamName()).isEqualTo("Team");
    }

    @Test
    public void findAllByUsernameIn() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> findAll = memberRepository.findAllByUsernameIn(List.of("member1", "member2"));
        assertThat(findAll).containsExactly(member1, member2);
    }

    @Test
    public void paging() {
        Team team = new Team("Team");
        teamRepository.save(team);

        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 20);
        Member member4 = new Member("member4", 20);
        Member member5 = new Member("member5", 20);
        member1.changeTeam(team);
        member2.changeTeam(team);
        member3.changeTeam(team);
        member4.changeTeam(team);
        member5.changeTeam(team);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        PageRequest pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> result = memberRepository.findByAgeWithQuery(20, pageable);

        assertThat(result.getContent()).containsExactly(member5, member4, member3);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.isFirst()).isTrue();
    }

    @Test
    void entityGraphTest() {
        Team team1 = new Team("Team1");
        Team team2 = new Team("Team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 20);
        Member member4 = new Member("member4", 20);
        Member member5 = new Member("member5", 20);
        member1.changeTeam(team1);
        member2.changeTeam(team1);
        member3.changeTeam(team1);
        member4.changeTeam(team2);
        member5.changeTeam(team2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        em.flush();
        em.clear();

        List<Member> result = memberRepository.findWithTeamBy();
//        List<Member> result = memberRepository.findAll();
        for (Member member : result) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        Member member1 = new Member("member1", 20);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername()).orElseThrow();
        findMember.changeUsername("member2");

        em.flush();
    }

    @Test
    public void lockTest() {
        Member member1 = new Member("member1", 20);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findLockByUsername(member1.getUsername()).orElseThrow();
        findMember.changeUsername("member2");

        em.flush();

        List<Member> allMember = memberRepository.findAll();
        for (Member member : allMember) {
            System.out.println("member = " + member);
            System.out.println("member.getCreatedDate() = " + member.getCreatedDate());
            System.out.println("member.getCreatedBy() = " + member.getCreatedBy());
            System.out.println("member.getLastModifiedDate() = " + member.getLastModifiedDate());
            System.out.println("member.getLastModifiedBy() = " + member.getLastModifiedBy());
        }
    }

    @Test
    public void customTest() {
        Member member1 = new Member("member1", 20);
        memberRepository.save(member1);

        List<Member> members = memberRepository.findMemberCustom();
        for (Member member : members) {
            System.out.println(member);
        }
    }

    @Test
    public void projectionTest() {
        Team team1 = new Team("team1");

        teamRepository.save(team1);

        Member member1 = new Member("member1", 20, team1);
        Member member2 = new Member("member2", 20, team1);

        member1.changeTeam(team1);
        member2.changeTeam(team1);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<UsernameOnly> findMembers = memberRepository.findProjectionsByUsername("member1");
        for (UsernameOnly findMember : findMembers) {
            System.out.println("findMember = " + findMember);
            System.out.println("findMember.getClass() = " + findMember.getClass());
            System.out.println("findMember.getUsername() = " + findMember.getUsername());
        }

        List<UsernameOnlyDto> findDtoList =
            memberRepository.findProjectionsWithDtoByUsername("member1", UsernameOnlyDto.class);
        for (UsernameOnlyDto usernameOnlyDto : findDtoList) {
            System.out.println("usernameOnlyDto = " + usernameOnlyDto);
            System.out.println("usernameOnlyDto.getClass() = " + usernameOnlyDto.getClass());
            System.out.println("usernameOnlyDto.getUsername() = " + usernameOnlyDto.getUsername());
        }

        List<NestedClosedProjection> nestedProjectionFind =
            memberRepository.findProjectionsWithDtoByUsername("member1", NestedClosedProjection.class);
        for (NestedClosedProjection nestedClosedProjection : nestedProjectionFind) {
            System.out.println("nestedClosedProjection = " + nestedClosedProjection);
            System.out.println("nestedClosedProjection.getClass() = " + nestedClosedProjection.getClass());
            System.out.println("nestedClosedProjection.getUsername() = " + nestedClosedProjection.getUsername());
            System.out.println("nestedClosedProjection.getTeam().getClass() = "
                               + nestedClosedProjection.getTeam().getClass());
            System.out.println("nestedClosedProjection.getTeam().getName() = "
                               + nestedClosedProjection.getTeam().getName());
        }
    }

    @Test
    public void nativeQueryTest() {
        Team team1 = new Team("team1");

        teamRepository.save(team1);

        Member member1 = new Member("member1", 20, team1);
        Member member2 = new Member("member2", 20, team1);

        member1.changeTeam(team1);
        member2.changeTeam(team1);

        memberRepository.save(member1);
        memberRepository.save(member2);

        PageRequest pageRequest = PageRequest.of(0, 5);

        Page<MemberProjection> findMembers = memberRepository.findByNativeProjection(pageRequest);
        for (MemberProjection findMember : findMembers) {
            System.out.println("findMember = " + findMember);
        }
    }
}