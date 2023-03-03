package springhello.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springhello.core.member.Grade;
import springhello.core.member.Member;
import springhello.core.member.MemberService;
import springhello.core.order.Order;
import springhello.core.order.OrderService;

public class OrderApplication {

    public static void main(String[] args) {
//        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
//        MemberService memberService = ac.getBean("memberService", MemberService.class);
//        OrderService orderService = ac.getBean("orderService", OrderService.class);
        MemberService memberService = ac.getBean(MemberService.class);
        OrderService orderService = ac.getBean(OrderService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        Order order = orderService.createOrder(findMember.getId(), "spring lecture A", 10000);

        System.out.println(order);
    }
}
