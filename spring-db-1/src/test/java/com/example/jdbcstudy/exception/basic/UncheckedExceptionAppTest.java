package com.example.jdbcstudy.exception.basic;

import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UncheckedExceptionAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request)
                .isInstanceOf(RuntimeSQLException.class)
                .hasCauseInstanceOf(SQLException.class)
                .hasCause(new SQLException("Artificial SQLException."));
    }

    static class Controller {
        private final Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        private final NetworkClient networkClient = new NetworkClient();
        private final Repository repository = new Repository();

        /**
         * The unchecked exception do not need {@code throws} clause so it does not pollute upper layers dependency.
         */
        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            try {
                connect();
            } catch (ConnectException e) {
                // wrapped by a runtime exception.
                throw new RuntimeConnectException(e);
            }
        }

        private void connect() throws ConnectException {
            throw new ConnectException("Artificial ConnectException.");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSql();
            } catch (SQLException e) {
                // wrapped by a runtime exception.
                throw new RuntimeSQLException(e);
            }
        }

        private void runSql() throws SQLException {
            throw new SQLException("Artificial SQLException.");
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(SQLException cause) {
            super(cause);
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(ConnectException cause) {
            super(cause);
        }
    }

}
