package com.example.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    @TestConfiguration
    static class Config {

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

    }

    @Test
    void commit() {
        log.info("[Start transaction]");
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("[Commit transaction]");
        transactionManager.commit(status);

        log.info("[Complete committing transaction]");
    }

    @Test
    void rollback() {
        log.info("[Start transaction]");
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("[roll back transaction]");
        transactionManager.rollback(status);

        log.info("[Complete rolling back transaction]");
    }

    @Test
    void doubleCommit() {
        log.info("[Start tx1]");
        TransactionStatus tx1 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("[Commit tx1]");
        transactionManager.commit(tx1);

        log.info("[Start tx2]");
        TransactionStatus tx2 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("[Commit tx2]");
        transactionManager.commit(tx2);
    }

    @Test
    void doubleCommitRollback() {
        log.info("[Start tx1]");
        TransactionStatus tx1 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("[Commit tx1]");
        transactionManager.commit(tx1);

        log.info("[Start tx2]");
        TransactionStatus tx2 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("[Roll back tx2]");
        transactionManager.rollback(tx2);
    }

    @Test
    void innerCommit() {
        log.info("[Start outer tx]");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction());

        log.info("[Start inner tx]");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction());

        log.info("[Commit inner tx]");
        transactionManager.commit(inner);

        log.info("[Commit outer tx]");
        transactionManager.commit(outer);
    }

    @Test
    void outerRollback() {
        log.info("[Start outer tx]");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("[Start inner tx]");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("[Commit inner tx]");
        transactionManager.commit(inner);

        log.info("[rollback outer tx]");
        transactionManager.rollback(outer);
    }

    @Test
    void innerRollback() {
        log.info("[Start outer tx]");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("[Start inner tx]");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionAttribute());

        log.info("[Rollback inner tx]");
        transactionManager.rollback(inner); // Transaction is marked as rollback-only

        log.info("[Commit outer tx]");
        assertThatThrownBy(() -> transactionManager.commit(outer))
                .isExactlyInstanceOf(UnexpectedRollbackException.class);
    }

    @Test
    void innerRollbackRequiresNew() {
        log.info("[Start outer tx]");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction());

        log.info("[Start inner tx]");
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus inner = transactionManager.getTransaction(attribute);
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction());

        log.info("[Rollback inner tx]");
        transactionManager.rollback(inner);

        log.info("[Commit outer tx]");
        transactionManager.commit(outer);
    }

}
