package springhello.core.discount;

import org.springframework.stereotype.Component;
import springhello.core.annotation.MainDiscountPolicy;
import springhello.core.member.Grade;
import springhello.core.member.Member;

@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {

    private final int discountRate;

    public RateDiscountPolicy() {
        this.discountRate = 10;
    }

    public RateDiscountPolicy(int discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountRate / 100;
        } else {
            return 0;
        }
    }
}
