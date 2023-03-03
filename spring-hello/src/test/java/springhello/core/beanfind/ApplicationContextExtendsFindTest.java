package springhello.core.beanfind;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springhello.core.discount.DiscountPolicy;
import springhello.core.discount.FixDiscountPolicy;
import springhello.core.discount.RateDiscountPolicy;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    void findBeanByParentTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    void findBeanByBeanNameWithParentTypeDuplicate() {
        DiscountPolicy fixDiscountPolicy = ac.getBean("fixDiscountPolicy", DiscountPolicy.class);
        assertThat(fixDiscountPolicy).isInstanceOf(FixDiscountPolicy.class);
    }

    @Test
    void findBeanBySubtype() {
        RateDiscountPolicy discountPolicy = ac.getBean(RateDiscountPolicy.class);
        assertThat(discountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    void findAllBeansByParentType() {
        Map<String, DiscountPolicy> discountPolicies = ac.getBeansOfType(DiscountPolicy.class);
        for (String beanName : discountPolicies.keySet()) {
            System.out.println("discountPolicies.get(beanName) = " + discountPolicies.get(beanName));
        }
        assertThat(discountPolicies.size()).isEqualTo(2);
    }

    @Test
    void findAllBeansByObjectType() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String beanName : beansOfType.keySet()) {
            System.out.println("discountPolicies.get(beanName) = " + beansOfType.get(beanName));
        }
    }

    @Configuration
    static class TestConfig {

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy(1000);
        }

        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy(10);
        }
    }
}
