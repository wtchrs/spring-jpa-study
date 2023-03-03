package springhello.core.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import springhello.core.discount.DiscountPolicy;
import springhello.core.discount.FixDiscountPolicy;
import springhello.core.member.*;

public class OrderServiceTest {

    MemberRepository memberRepository = new MemoryMemberRepository();
    MemberService memberService = new MemberServiceImpl(memberRepository);
    DiscountPolicy discountPolicy = new FixDiscountPolicy(1000);
    OrderService orderService = new OrderServiceImpl(memberRepository, discountPolicy);

    @Test
    void createOrder() {
        Long memberId =1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
        Assertions.assertThat(order.calculatePrice()).isEqualTo(9000);
    }
}
