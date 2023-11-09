package com.example.springtx.apply;

import org.slf4j.Logger;
import org.springframework.transaction.support.TransactionSynchronizationManager;

abstract class TxUtils {

    public static void printTxInfo(Logger log) {
        boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("txActive = {}", txActive);
    }

    static void printTxReadOnlyInfo(Logger log) {
        boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        log.info("txActive = {}", txActive);
        log.info("readOnly = {}", readOnly);
    }

}
