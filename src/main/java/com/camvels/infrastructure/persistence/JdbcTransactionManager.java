package com.camvels.infrastructure.persistence;

import com.camvels.domain.port.out.TransactionPort;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransactionManager implements TransactionPort {

    private final JdbcConnectionProvider connectionProvider;

    public JdbcTransactionManager(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void executeInTransaction(Runnable action) {
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            JdbcConnectionProvider.bind(connection);
            try {
                action.run();
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                throw e;
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                JdbcConnectionProvider.unbind();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en transacción", e);
        }
    }
}
