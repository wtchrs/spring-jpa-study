package com.example.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService service;

    @Test
    void runtimeExceptionTest() {
        // must be rolled back
        assertThatThrownBy(() -> service.runtimeException())
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedExceptionTest() {
        // must be committed
        assertThatThrownBy(() -> service.checkedException())
                .isExactlyInstanceOf(MyException.class);
    }

    @Test
    void checkedExceptionRollbackTest() {
        // must be rolled back
        assertThatThrownBy(() -> service.checkedExceptionRollback())
                .isExactlyInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollbackTestConfig {

        @Bean
        public RollbackService rollbackService() {
            return new RollbackService();
        }

    }

    @Slf4j
    static class RollbackService {

        @Transactional
        public void runtimeException() {
            log.info("RollbackService.runtimeException");
            throw new RuntimeException();
        }

        @Transactional
        public void checkedException() throws MyException {
            log.info("RollbackService.checkedException");
            throw new MyException();
        }

        @Transactional(rollbackFor = MyException.class)
        public void checkedExceptionRollback() throws MyException {
            log.info("RollbackService.runtimeExceptionRollback");
            throw new MyException();
        }

    }

    static class MyException extends Exception {
    }

}
