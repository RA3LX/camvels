package com.camvels.infrastructure.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcConnectionProvider {

    private static final ThreadLocal<Connection> TRANSACTIONAL = new ThreadLocal<>();

    private final DataSource dataSource;

    public JdbcConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = TRANSACTIONAL.get();
        if (conn != null) {
            return conn;
        }
        return dataSource.getConnection();
    }

    public static void bind(Connection connection) {
        TRANSACTIONAL.set(connection);
    }

    public static void unbind() {
        TRANSACTIONAL.remove();
    }
}
