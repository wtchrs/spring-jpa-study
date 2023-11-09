package com.example.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class InternalCallTestV2 {

    @Autowired
    CallService callService;

    @Autowired
    InternalService internalService;

    @Test
    void printProxy() {
        log.info("callService.getClass() = {}", callService.getClass());
        log.info("internalService.getClass() = {}", internalService.getClass());
    }

    @Test
    void internalCall() {
        internalService.internal();
    }

    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallTestV1Config {

        @Bean
        public InternalService internalService() {
            return new InternalService();
        }

        @Bean
        public CallService callService() {
            return new CallService(internalService());
        }

    }

    @RequiredArgsConstructor
    static class CallService {

        private final InternalService internalService;

        public void external() {
            log.info("CallService.external");
            TxUtils.printTxInfo(log);
            internalService.internal();
        }

    }

    static class InternalService {

        @Transactional
        public void internal() {
            log.info("CallService.internal");
            TxUtils.printTxInfo(log);
        }

    }

}
