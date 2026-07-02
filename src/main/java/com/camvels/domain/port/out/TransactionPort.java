package com.camvels.domain.port.out;

@FunctionalInterface
public interface TransactionPort {
    void executeInTransaction(Runnable action);
}
