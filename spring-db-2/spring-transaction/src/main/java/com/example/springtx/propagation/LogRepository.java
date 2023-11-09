package com.example.springtx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {

    private final EntityManager em;

    @Transactional
    public void save(Log logMessage) {
        log.info("[Save log] {}", logMessage);
        em.persist(logMessage);

        if (logMessage.getMessage().contains("artificial_exception")) {
            log.info("Artificial exception occurred.");
            throw new RuntimeException("Artificial exception occurred.");
        }
    }

    public void saveWithoutTx(Log logMessage) {
        log.info("[Save log] {}", logMessage);
        em.persist(logMessage);

        if (logMessage.getMessage().contains("artificial_exception")) {
            log.info("[Artificial exception occurred.]");
            throw new RuntimeException("Artificial exception occurred.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveWithRequiresNew(Log logMessage) {
        log.info("[Save log] {}", logMessage);
        em.persist(logMessage);

        if (logMessage.getMessage().contains("artificial_exception")) {
            log.info("Artificial exception occurred.");
            throw new RuntimeException("Artificial exception occurred.");
        }
    }

    @Transactional(readOnly = true)
    public Optional<Log> find(String message) {
        return em.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultStream()
                .findAny();
    }

}
