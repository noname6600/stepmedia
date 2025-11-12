package com.thanhvh.jpa.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * MasterReplicaRoutingDataSource
 */
public class MasterReplicaRoutingDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<Type> currentDataSource = new ThreadLocal<>();

    /**
     * @param master  master data source
     * @param replica replica data source
     */
    MasterReplicaRoutingDataSource(DataSource master, DataSource replica) {
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(Type.MASTER, master);
        dataSources.put(Type.REPLICA, replica);

        super.setTargetDataSources(dataSources);
        super.setDefaultTargetDataSource(master);
    }

    static void setReadonlyDataSource(boolean isReadonly) {
        currentDataSource.set(isReadonly ? Type.REPLICA : Type.MASTER);
    }

    static boolean isCurrentlyReadonly() {
        return currentDataSource.get() == Type.REPLICA;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return currentDataSource.get();
    }

    private enum Type {
        MASTER, REPLICA
    }
}
