package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.support.MemberSearchConditionUtils;
import study.querydsl.support.Querydsl4RepositorySupport;

import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;
import static study.querydsl.support.MemberSearchConditionUtils.toBooleanBuilder;

@Repository
public class MemberTestRepository extends Querydsl4RepositorySupport {

    public MemberTestRepository() {
        super(Member.class);
    }

    public List<Member> findAllWithSelect() {
        return select(member)
            .from(member)
            .fetch();
    }

    public List<Member> findAllWithSelectFrom() {
        return selectFrom(member)
            .fetch();
    }

    public Page<MemberTeamDto> searchPageByApplyPagination(MemberSearchCondition condition, Pageable pageable) {
        return applyPagination(
            pageable,
            select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(toBooleanBuilder(condition)),
            select(member.count())
                .from(member)
                .where(toBooleanBuilder(condition))
        );
    }
}
