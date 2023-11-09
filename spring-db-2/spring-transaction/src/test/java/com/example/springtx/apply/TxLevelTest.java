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
public class TxLevelTest {

    @Autowired
    LevelService levelService;

    @Test
    void txLevelTest() {
        levelService.write();
        levelService.read();
    }

    @TestConfiguration
    static class TxLevelTestConfig {

        @Bean
        public LevelService levelService() {
            return new LevelService();
        }

    }

    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {

        @Transactional
        public void write() {
            log.info("LevelService.write");
            TxUtils.printTxReadOnlyInfo(log);
        }

        public void read() {
            log.info("LevelService.read");
            TxUtils.printTxReadOnlyInfo(log);
        }

    }

}
