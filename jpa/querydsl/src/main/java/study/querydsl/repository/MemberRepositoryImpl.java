package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;
import static study.querydsl.support.MemberSearchConditionUtils.toBooleanBuilder;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
            .select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name))
            .from(member)
            .leftJoin(member.team, team)
            .where(toBooleanBuilder(condition))
            .fetch();
    }

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> fetch = queryFactory
            .select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name))
            .from(member)
            .leftJoin(member.team, team)
            .where(toBooleanBuilder(condition))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(member.count())
            .from(member)
            .where(toBooleanBuilder(condition))
            .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> fetch = queryFactory
            .select(new QMemberTeamDto(member.id, member.username, member.age, team.id, team.name))
            .from(member)
            .leftJoin(member.team, team)
            .where(toBooleanBuilder(condition))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(member.count())
            .from(member)
            .where(toBooleanBuilder(condition));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);

    }

}
