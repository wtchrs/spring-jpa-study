package project.springbootexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.springbootexample.domain.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    Optional<Member> findByName(String name);
}
