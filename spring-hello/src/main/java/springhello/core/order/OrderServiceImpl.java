package springhello.core.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springhello.core.annotation.MainDiscountPolicy;
import springhello.core.discount.DiscountPolicy;
import springhello.core.member.Member;
import springhello.core.member.MemberRepository;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member findMember = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(findMember, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    /** For test code */
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
