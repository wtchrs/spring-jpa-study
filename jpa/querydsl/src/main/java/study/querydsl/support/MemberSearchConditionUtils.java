package study.querydsl.support;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

public abstract class MemberSearchConditionUtils {

    public static BooleanBuilder toBooleanBuilder(MemberSearchCondition condition) {
        return new BooleanBuilder()
            .and(usernameEq(condition.getUsername()))
            .and(teamNameEq(condition.getTeamName()))
            .and(ageGoe(condition.getAgeGoe()))
            .and(ageLoe(condition.getAgeLoe()));
    }

    public static BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    public static BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    public static BooleanExpression ageGoe(Integer age) {
        return age != null ? member.age.goe(age) : null;
    }

    public static BooleanExpression ageLoe(Integer age) {
        return age != null ? member.age.loe(age) : null;
    }
}
