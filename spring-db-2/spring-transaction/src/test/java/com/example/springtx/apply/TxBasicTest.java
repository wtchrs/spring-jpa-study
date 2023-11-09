package com.example.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck() {
        log.info("basicService = {}", basicService.getClass());
        boolean aopProxy = AopUtils.isAopProxy(basicService);
        log.info("aopProxy = {}", aopProxy);
        assertThat(aopProxy).isTrue();
    }

    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {

        @Bean
        public BasicService basicService() {
            return new BasicService();
        }

    }

    @Slf4j
    static class BasicService {

        @Transactional
        public void tx() {
            log.info("BasicService.tx");
            TxUtils.printTxInfo(log);
        }

        public void nonTx() {
            log.info("BasicService.nonTx");
            TxUtils.printTxInfo(log);
        }

    }

}
