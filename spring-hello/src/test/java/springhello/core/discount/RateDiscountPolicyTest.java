package springhello.core.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import springhello.core.member.Grade;
import springhello.core.member.Member;

import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    DiscountPolicy discountPolicy = new RateDiscountPolicy(10);

    @Test
    public void discountVipMember() {
        // given
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when
        int discount = discountPolicy.discount(member, 10000);

        // then
        Assertions.assertThat(discount).isEqualTo(1000);
    }

    @Test
    public void discountBasicMember() {
        // given
        Member member = new Member(2L, "memberA", Grade.BASIC);

        // when
        int discount = discountPolicy.discount(member, 10000);

        // then
        Assertions.assertThat(discount).isEqualTo(0);
    }
}