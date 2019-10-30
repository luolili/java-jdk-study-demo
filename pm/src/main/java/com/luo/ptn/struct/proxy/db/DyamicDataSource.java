package com.luo.ptn.struct.proxy.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DyamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDBType();
    }
}
