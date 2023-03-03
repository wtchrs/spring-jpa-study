package springhello.core.discount;

import org.springframework.stereotype.Component;
import springhello.core.member.Grade;
import springhello.core.member.Member;

@Component
public class FixDiscountPolicy implements DiscountPolicy {

    private final int discountFixAmount;

    public FixDiscountPolicy() {
        this(1000);
    }

    public FixDiscountPolicy(int discountFixAmount) {
        this.discountFixAmount = discountFixAmount;
    }

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
