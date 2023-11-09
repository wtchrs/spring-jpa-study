package com.example.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class InternalCallTestV1 {

    @Autowired
    CallService callService;

    @Test
    void printProxy() {
        log.info("callService.getClass() = {}", callService.getClass());
    }

    @Test
    void internalCall() {
        callService.internal();
    }

    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallTestV1Config {

        @Bean
        public CallService callService() {
            return new CallService();
        }

    }

    static class CallService {

        public void external() {
            log.info("CallService.external");
            TxUtils.printTxInfo(log);
            internal(); // not a proxy's internal(), but CallService.internal()
        }

        @Transactional
        public void internal() {
            log.info("CallService.internal");
            TxUtils.printTxInfo(log);
        }

    }

}
