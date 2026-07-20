package com.camvels.infrastructure.persistence;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
            return nonClosing(conn);
        }
        return dataSource.getConnection();
    }

    public static void bind(Connection connection) {
        TRANSACTIONAL.set(connection);
    }

    public static void unbind() {
        TRANSACTIONAL.remove();
    }

    private static Connection nonClosing(Connection delegate) {
        InvocationHandler handler = (proxy, method, args) -> {
            if ("close".equals(method.getName())) {
                return null;
            }
            return method.invoke(delegate, args);
        };
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                handler);
    }
}
