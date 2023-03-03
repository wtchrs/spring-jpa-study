package springhello.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springhello.core.discount.DiscountPolicy;
import springhello.core.discount.FixDiscountPolicy;
import springhello.core.member.MemberRepository;
import springhello.core.member.MemberService;
import springhello.core.member.MemberServiceImpl;
import springhello.core.member.MemoryMemberRepository;
import springhello.core.order.OrderService;
import springhello.core.order.OrderServiceImpl;

@Configuration
public class AppConfig {

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy(1000);
    }

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
}
