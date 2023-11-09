package com.example.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class InitTxTest {

    @Autowired
    InitTestService service;

    @Test
    void initTest() {
    }

    @TestConfiguration
    static class InitTxTestConfig {

        @Bean
        public InitTestService initTestService() {
            return new InitTestService();
        }

    }

    static class InitTestService {

        @PostConstruct
        @Transactional
        public void initV1() {
            log.info("InitTestService.initV1");
            TxUtils.printTxInfo(log);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            log.info("InitTestService.initV2");
            TxUtils.printTxInfo(log);
        }

    }

}
