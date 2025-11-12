package com.thanhvh.jpa.datasource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * ReplicaAwareTransactionManager
 */
public class ReplicaAwareTransactionManager implements PlatformTransactionManager {
    private final PlatformTransactionManager delegated;

    /**
     * @param delegated {@link PlatformTransactionManager}
     */
    ReplicaAwareTransactionManager(PlatformTransactionManager delegated) {
        this.delegated = delegated;
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        boolean isTxActive = TransactionSynchronizationManager.isActualTransactionActive();
        if (!isTxActive) {
            MasterReplicaRoutingDataSource.setReadonlyDataSource(definition.isReadOnly());
        }

        return delegated.getTransaction(definition);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        delegated.commit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        delegated.rollback(status);
    }
}
