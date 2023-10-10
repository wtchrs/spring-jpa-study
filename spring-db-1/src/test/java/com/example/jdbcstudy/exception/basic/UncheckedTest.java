package com.example.jdbcstudy.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {

    @Test
    void uncheckedCatch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void uncheckedThrow() {
        Service service = new Service();
        Assertions.assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyUncheckedException.class)
                .hasMessage("Artificial Unchecked Exception.");
    }

    /**
     * The exception that extends {@link RuntimeException} becomes an runtime exception.
     */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class Repository {
        public void call() {
            throw new MyUncheckedException("Artificial Unchecked Exception.");
        }
    }

    static class Service {
        private final Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                log.info("MyUncheckedException occurred: {}", e.getMessage(), e);
            }
        }

        /**
         * The {@code throws} clause is not required for methods that throw runtime exceptions.
         */
        public void callThrow() {
            repository.call();
        }
    }
}
