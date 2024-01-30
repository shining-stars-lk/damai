package com.example.shardingsphere;

import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.apache.shardingsphere.sharding.exception.algorithm.sharding.ShardingAlgorithmInitializationException;

import java.util.Collection;
import java.util.Properties;

public class HashModShardingAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {
    
    private static final String totalTableCountName = "totalTableCount";
    
    private static final String totalTableCountExceptionMessage = "total table count is empty";
    
    private static final String databaseTableCountName = "databaseTableCount";
    
    private static final String databaseTableCountExceptionMessage = "database table count is empty";
    private int totalTableCount;
    private int databaseTableCount;
    
    @Override
    public void init(final Properties props) {
        ShardingSpherePreconditions.checkState(props.containsKey(totalTableCountName), () -> new ShardingAlgorithmInitializationException(getType(), totalTableCountExceptionMessage));
        ShardingSpherePreconditions.checkState(props.containsKey(databaseTableCountName), () -> new ShardingAlgorithmInitializationException(getType(), databaseTableCountExceptionMessage));
        this.totalTableCount = Integer.parseInt(props.getProperty(totalTableCountName));
        this.databaseTableCount = Integer.parseInt(props.getProperty(databaseTableCountName));
    }
    
    @Override
    public String doSharding(final Collection<String> collection, final PreciseShardingValue<Comparable<?>> preciseShardingValue) {
        return null;
    }
    
    @Override
    public Collection<String> doSharding(final Collection<String> collection, final RangeShardingValue<Comparable<?>> rangeShardingValue) {
        return null;
    }
    
    @Override
    public String getType() {
        return "CLASS_BASED";
    }
}
