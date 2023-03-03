package springhello.core.discount;

import springhello.core.member.Member;

public interface DiscountPolicy {

    /**
     * @return discount price
     */
    int discount(Member member, int price);
}
