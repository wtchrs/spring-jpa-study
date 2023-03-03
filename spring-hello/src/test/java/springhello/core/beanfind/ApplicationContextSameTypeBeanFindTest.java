package springhello.core.beanfind;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springhello.core.member.MemberRepository;
import springhello.core.member.MemoryMemberRepository;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextSameTypeBeanFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    void findBeanByTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class, ()-> ac.getBean(MemberRepository.class));
    }

    @Test
    void findBeanByName() {
        Object memberRepository = ac.getBean("memberRepository1");
        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }

    @Test
    void findAllBeansByType() {
        Map<String, MemberRepository> beans = ac.getBeansOfType(MemberRepository.class);
        for (String beanName : beans.keySet()) {
            System.out.println("beans.get(beanName) = " + beans.get(beanName));
        }
        System.out.println("beans = " + beans);
        assertThat(beans.size()).isEqualTo(2);
    }

    @Configuration
    static class TestConfig{

        @Bean
        MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }

        @Bean
        MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }
}
