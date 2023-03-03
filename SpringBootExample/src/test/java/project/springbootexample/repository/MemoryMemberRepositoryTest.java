package project.springbootexample.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import project.springbootexample.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryMemberRepositoryTest {

    private final MemoryMemberRepository repo = new MemoryMemberRepository();

    @AfterEach
    void afterEach() {
        repo.clear();
    }

    @Test
    void save() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        Member result = repo.save(member);

        // then
        assertThat(result).isEqualTo(member);
    }

    @Test
    void findById() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring2");

        Member member3 = new Member();
        member3.setName("spring3");

        // when
        repo.save(member1);
        repo.save(member2);
        repo.save(member3);

        // then
        assertThat(repo.findById(1L).get()).isEqualTo(member1);
        assertThat(repo.findById(2L).get()).isEqualTo(member2);
        assertThat(repo.findById(3L).get()).isEqualTo(member3);
    }

    @Test
    void findByName() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring2");

        Member member3 = new Member();
        member3.setName("spring3");

        // when
        repo.save(member1);
        repo.save(member2);
        repo.save(member3);

        // then
        assertThat(repo.findByName("spring1").get()).isEqualTo(member1);
        assertThat(repo.findByName("spring2").get()).isEqualTo(member2);
        assertThat(repo.findByName("spring3").get()).isEqualTo(member3);
    }

    @Test
    void findAll() {
        // given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring2");

        Member member3 = new Member();
        member3.setName("spring3");

        // when
        repo.save(member1);
        repo.save(member2);
        repo.save(member3);
        List<Member> results = repo.findAll();

        // then
        assertThat(results).contains(member1, member2, member3);
    }
}