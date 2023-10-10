package com.example.jdbcstudy.exception.basic;

import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

public class CheckedExceptionAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request)
                .isInstanceOf(SQLException.class)
                .hasMessage("Artificial SQLException.");
    }

    static class Controller {
        private final Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {
        private final NetworkClient networkClient = new NetworkClient();
        private final Repository repository = new Repository();

        /**
         * The checked exception {@code throws} clause propagates the dependency of the exception to higher layers. If
         * the exception is unrecoverable and is not wrapped by a runtime exception, it impacts all methods that
         * directly or indirectly call this method.
         * When the implementation changes, you must update not only the associated modules but also the {@code throws}
         * clauses that throw checked exceptions in upper layers.
         *
         * @throws SQLException     occurred by {@link Repository}.
         * @throws ConnectException occurred by {@link NetworkClient}.
         */
        public void logic() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("Artificial ConnectException.");
        }
    }

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("Artificial SQLException.");
        }
    }
}
