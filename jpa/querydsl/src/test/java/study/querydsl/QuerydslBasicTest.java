package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void beforeEach() {
        queryFactory = new JPAQueryFactory(em);
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
    public void startJpql() {
        Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                          .setParameter("username", "member1").getSingleResult();

        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(result.getAge()).isEqualTo(10);
        assertThat(result.getTeam().getName()).isEqualTo("TeamA");
    }

    @Test
    public void startQuerydsl() {
        Member result = queryFactory.selectFrom(member).where(member.username.eq("member1")).fetchOne();

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(result.getAge()).isEqualTo(10);
        assertThat(result.getTeam().getName()).isEqualTo("TeamA");
    }

    @Test
    public void search() {
        Member result = queryFactory.selectFrom(member)
                                    .where(member.username.eq("member1").and(member.age.eq(10)))
                                    .fetchOne();

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(result.getAge()).isEqualTo(10);
        assertThat(result.getTeam().getName()).isEqualTo("TeamA");
    }

    @Test
    public void searchAnd() {
        Member result = queryFactory.selectFrom(member)
                                    .where(
                                        member.username.eq("member1"),
                                        member.age.eq(10)
                                    )
                                    .fetchOne();

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(result.getAge()).isEqualTo(10);
        assertThat(result.getTeam().getName()).isEqualTo("TeamA");
    }

    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> fetch = queryFactory
            .selectFrom(member)
            .orderBy(
                member.age.desc(),
                member.username.asc().nullsLast()
            ).fetch();

        for (Member m : fetch) {
            System.out.println(m);
        }

        assertThat(fetch.get(0).getUsername()).isEqualTo("member5");
        assertThat(fetch.get(1).getUsername()).isEqualTo("member6");
        assertThat(fetch.get(2).getUsername()).isNull();
    }

    @Test
    public void paging1() {
        List<Member> fetch = queryFactory
            .selectFrom(member)
            .orderBy(
                member.username.asc().nullsLast()
            )
            .offset(0)
            .limit(2)
            .fetch();

        assertThat(fetch).hasSize(2);
    }

    @Test
    public void paging2() {
        List<Member> fetch = queryFactory
            .selectFrom(member)
            .orderBy(
                member.username.asc().nullsLast()
            )
            .offset(0)
            .limit(2)
            .fetch();

        assertThat(fetch).hasSize(2);
    }

    @Test
    public void aggregation() {
        Tuple tuple = queryFactory
            .select(
                member.count(),
                member.age.sum(),
                member.age.avg(),
                member.age.min(),
                member.age.max())
            .from(member)
            .fetchOne();

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
    }

    @Test
    public void group() {
        List<Tuple> result = queryFactory
            .select(team.name, member.age.avg())
            .from(member)
            .join(member.team, team)
            .groupBy(team.name)
            .orderBy(team.name.asc())
            .fetch();

        Tuple team1 = result.get(0);
        Tuple team2 = result.get(1);

        assertThat(team1.get(team.name)).isEqualTo("TeamA");
        assertThat(team1.get(member.age.avg())).isEqualTo(15);
        assertThat(team2.get(team.name)).isEqualTo("TeamB");
        assertThat(team2.get(member.age.avg())).isEqualTo(35);
    }

    @Test
    public void join() {
        List<Member> result = queryFactory
            .selectFrom(member)
            .join(member.team, team)
            .where(team.name.eq("TeamA"))
            .fetch();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("username").containsExactly("member1", "member2");

        for (Member member1 : result) {
            assertThat(member1.getTeam().getName()).isEqualTo("TeamA");
        }
    }

    @Test
    public void thetaJoin() {
        em.persist(new Member("TeamA", 10));
        em.persist(new Member("TeamB", 10));

        List<Member> result = queryFactory
            .select(member)
            .from(member, team)
            .where(member.username.eq(team.name))
            .orderBy(member.username.asc())
            .fetch();

        assertThat(result).extracting("username").containsExactly("TeamA", "TeamB");

        assertThat(result.get(0).getUsername()).isEqualTo("TeamA");
        assertThat(result.get(1).getUsername()).isEqualTo("TeamB");
    }

    @Test
    public void joinOnFiltering() {
        List<Tuple> result = queryFactory
            .select(member, team)
            .from(member)
            .leftJoin(member.team, team).on(team.name.eq("TeamA"))
            .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
            assertThat(tuple.get(team)).is(anyOf(
                new Condition<>(Objects::isNull, "null"),
                new Condition<>(t -> t != null && t.getName().equals("TeamA"), "equal to TeamA")
            ));
        }
    }

    @Test
    public void joinOnNoRelation() {
        em.persist(new Member("TeamA", 10));
        em.persist(new Member("TeamB", 10));

        List<Tuple> result = queryFactory
            .select(member, team)
            .from(member)
            .join(team).on(member.username.eq(team.name))
            .orderBy(member.username.asc())
            .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
            assertThat(tuple.get(member).getUsername()).is(
                new Condition<>(username -> username.equals("TeamA") || username.equals("TeamB"), "equals"));
        }
    }

    @Test
    public void withoutFetchJoin() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
            .selectFrom(member)
            .where(member.username.eq("member1"))
            .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("Without Fetch Join").isFalse();
    }

    @Test
    public void withFetchJoin() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
            .selectFrom(member)
            .join(member.team, team).fetchJoin()
            .where(member.username.eq("member1"))
            .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("With Fetch Join").isTrue();
    }

    @Test
    public void subQueryMax() {
        QMember subMember = new QMember("subMember");

        List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(
                JPAExpressions
                    .select(subMember.age.max())
                    .from(subMember)
            ))
            .fetch();

        assertThat(result).hasSize(1);
        assertThat(result).extracting("age").containsExactly(40);
    }

    @Test
    public void subQueryAvg() {
        QMember subMember = new QMember("subMember");

        List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.goe(
                JPAExpressions
                    .select(subMember.age.avg())
                    .from(subMember)
            ))
            .fetch();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("age").containsExactly(30, 40);
    }

    @Test
    public void simpleCase() {
        List<String> result = queryFactory
            .select(member.age
                        .when(10).then("Teenager")
                        .when(20).then("20's")
                        .otherwise("etc"))
            .from(member).fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void complexCase() {
        List<String> result = queryFactory
            .select(new CaseBuilder()
                        .when(member.age.between(10, 19)).then("Teenager")
                        .when(member.age.between(20, 29)).then("20's")
                        .otherwise("etc"))
            .from(member)
            .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void constant() {
        List<Tuple> result = queryFactory
            .select(member.username, Expressions.constant("Constant"))
            .from(member)
            .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void concat() {
        List<String> result = queryFactory
            .select(member.username.concat("_").concat(member.age.stringValue()))
            .from(member)
            .where(member.username.eq("member1"))
            .fetch();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo("member1_10");
    }

    @Test
    public void simpleProjection() {
        List<String> result = queryFactory
            .select(member.username)
            .from(member)
            .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void tupleProjection() {
        List<Tuple> result = queryFactory
            .select(member.username, member.age)
            .from(member)
            .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username + ", age = " + age);
        }
    }

    @Test
    public void findDtoByJPQL() {
        List<MemberDto> resultList =
            em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m",
                           MemberDto.class)
              .getResultList();

        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findDtoBySetter() {
        List<MemberDto> result = queryFactory
            .select(Projections.bean(MemberDto.class, member.username, member.age))
            .from(member)
            .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findDtoByFields() {
        List<MemberDto> result = queryFactory
            .select(Projections.fields(MemberDto.class, member.username, member.age))
            .from(member)
            .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
            .select(Projections.constructor(MemberDto.class, member.username, member.age))
            .from(member)
            .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findUserDtoBySetter() {
        List<UserDto> result = queryFactory
            .select(Projections.bean(UserDto.class, member.username.as("name"), member.age))
            .from(member)
            .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    public void findDtoByQueryProjectionAnnot() {
        List<MemberDto> result = queryFactory
            .select(new QMemberDto(member.username, member.age))
            .from(member)
            .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void dynamicQueryWithBooleanBuilder() {
        String username = "member1";
        Integer age = 10;

        List<Member> result = searchMember1(username, age);

        assertThat(result).hasSize(1);
        assertThat(result).extracting("username").containsExactly("member1");
        assertThat(result).extracting("age").containsExactly(10);
    }

    private List<Member> searchMember1(String username, Integer age) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!Objects.isNull(username)) builder.and(member.username.eq(username));
        if (!Objects.isNull(age)) builder.and(member.age.eq(age));

        return queryFactory
            .selectFrom(member)
            .where(builder)
            .fetch();
    }

    @Test
    public void dynamicQueryWithWhereParam() {
        String username = "member1";
        Integer age = 10;

        List<Member> result = searchMember2(username, age);

        assertThat(result).hasSize(1);
        assertThat(result).extracting("username").containsExactly("member1");
        assertThat(result).extracting("age").containsExactly(10);
    }

    private List<Member> searchMember2(String username, Integer age) {
        return queryFactory
            .selectFrom(member)
            .where(allEq(username, age))
            .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return username == null ? null : member.username.eq(username);
    }

    private BooleanExpression ageEq(Integer age) {
        return age == null ? null : member.age.eq(age);
    }

    private BooleanExpression allEq(String username, Integer age) {
        return usernameEq(username).and(ageEq(age));
    }

    @Test
    public void bulkUpdate() {
        long count = queryFactory
            .update(member)
            .set(member.username, "비회원")
            .where(member.age.lt(25))
            .execute();

        List<Member> beforeClear = queryFactory
            .selectFrom(member)
            .fetch();

        for (Member member1 : beforeClear) {
            // It seems that cache is synced with db.
            System.out.println("Before Clear = " + member1);
        }

        em.flush();
        em.clear();

        List<Member> afterClear = queryFactory
            .selectFrom(member)
            .fetch();

        for (Member member1 : afterClear) {
            System.out.println("After Clear = " + member1);
        }
    }

    @Test
    public void bulkAddAndMultiply() {
        long addCount = queryFactory
            .update(member)
            .set(member.age, member.age.add(1))
            .execute();

        long multiplyCount = queryFactory
            .update(member)
            .set(member.age, member.age.multiply(2))
            .execute();
    }

    @Test
    public void bulkDelete() {
        long count = queryFactory
            .delete(member)
            .where(member.age.gt(35))
            .execute();

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void sqlFunction1() {
        List<String> result = queryFactory
            .select(Expressions.stringTemplate(
                "function('replace', {0}, {1}, {2})",
                member.username, "member", "M"))
            .from(member)
            .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void sqlFunction2() {
        List<String> result = queryFactory
            .select(member.username)
            .from(member)
//            .where(member.username.eq(Expressions.stringTemplate("function('lower', {0})", member.username)))
            .where(member.username.eq(member.username.lower()))
            .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
}
