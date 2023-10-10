package com.example.jdbcstudy.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.jdbcstudy.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("con1 = {}, class = {}", con1, con1.getClass());
        log.info("con2 = {}, class = {}", con2, con2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        DataSource ds = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(ds);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        config.setPoolName("MyPool");
        DataSource ds = new HikariDataSource(config);

        useDataSource(ds);
        Thread.sleep(1000);
    }

    private static void useDataSource(DataSource ds) throws SQLException {
        Connection con1 = ds.getConnection();
        log.info("con1 = {}, class = {}", con1, con1.getClass());
        Connection con2 = ds.getConnection();
        log.info("con2 = {}, class = {}", con2, con2.getClass());
    }

}
