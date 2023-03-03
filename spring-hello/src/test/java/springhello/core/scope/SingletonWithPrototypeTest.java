package springhello.core.scope;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

public class SingletonWithPrototypeTest {

    @Test
    public void prototypeFind() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    public void singletonClientUsingPrototype() {
        ConfigurableApplicationContext ac =
            new AnnotationConfigApplicationContext(SingletonClient.class, PrototypeBean.class);

        SingletonClient client1 = ac.getBean(SingletonClient.class);
        int count1 = client1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        SingletonClient client2 = ac.getBean(SingletonClient.class);
        int count2 = client2.logic();
        Assertions.assertThat(count2).isEqualTo(1);
    }

    @RequiredArgsConstructor
    static class SingletonClient {

        private final Provider<PrototypeBean> prototypeBeanProvider;
        private final ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

        public int logic() {
            PrototypeBean prototypeBean1 = prototypeBeanProvider.get();
            PrototypeBean prototypeBean2 = prototypeBeanObjectProvider.getObject();

            prototypeBean1.addCount();
            return prototypeBean1.getCount();
        }
    }

    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init" + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy" + this);
        }
    }
}