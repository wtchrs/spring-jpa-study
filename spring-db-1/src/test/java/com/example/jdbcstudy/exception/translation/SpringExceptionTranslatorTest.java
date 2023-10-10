package com.example.jdbcstudy.exception.translation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.jdbcstudy.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
public class SpringExceptionTranslatorTest {
    DataSource ds = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

    /**
     * It is better translating low-level exceptions into high-level spring exception.
     * But it is difficult to translate exceptions to suit a bunch of databases
     * because the error codes vary depending on the database used.
     */
    @Test
    void sqlExceptionErrorCode() {
        String sql = "select grammar error";

        try {
            Connection con = ds.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.executeQuery();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            log.info("errorCode = {}", errorCode, e);
            // H2 DB column not found error code: 42122
            assertThat(errorCode).isEqualTo(42122);
        }
    }

    @Test
    void springSqlExceptionTranslator() {
        String sql = "select grammar error";

        try {
            Connection con = ds.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.executeQuery();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            log.info("errorCode = {}", errorCode);
            assertThat(errorCode).isEqualTo(42122);

            // Exception Translation
            var exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(ds);
            DataAccessException translated = exceptionTranslator.translate("select", sql, e);

            log.info("translated exception:", translated);
            assertThat(translated).isInstanceOf(BadSqlGrammarException.class);
        }
    }

}
