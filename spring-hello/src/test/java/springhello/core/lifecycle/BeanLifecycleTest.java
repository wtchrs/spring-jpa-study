package springhello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifecycleTest {
    
    @Test
    public void lifecycleTest() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifecycleConfig.class);
        NetworkClientMock networkClient = ac.getBean(NetworkClientMock.class);
        ac.close();
    }

    @Configuration
    static class LifecycleConfig{
        
        @Bean
        public NetworkClientMock networkClient() {
            NetworkClientMock networkClient = new NetworkClientMock();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
