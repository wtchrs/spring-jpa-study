package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findAsMemberDto();

    List<Member> findAllByUsernameIn(Collection<String> usernames);

    @Query("select m from Member m where m.username in :usernames")
    List<Member> findAllByUsernames(@Param("usernames") Collection<String> usernames);

    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join fetch m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAgeWithQuery(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @EntityGraph(attributePaths = {"team"})
    List<Member> findWithTeamBy();

    @Query("select m from Member m")
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberEntityGraph();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Optional<Member> findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Member> findLockByUsername(String username);

    List<UsernameOnly> findProjectionsByUsername(String username);

    List<UsernameOnlyDto> findProjectionsWithDtoByUsername(String username);

    <T> List<T> findProjectionsWithDtoByUsername(String username, Class<T> clazz);

    @Query(
        value = "select m.member_id as id, m.username, t.name as teamName"
                   + " from member m left join team t",
        countQuery = "select count(*) from member",
        nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
