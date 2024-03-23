package com.damai.shardingsphere;

import cn.hutool.core.collection.CollectionUtil;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单分库
 * @author: 阿宽不是程序员
 **/
public class DatabaseOrderComplexGeneArithmetic implements ComplexKeysShardingAlgorithm<Long> {
    private static final String SHARDING_COUNT_KEY_NAME = "sharding-count";
    
    private static final String TABLE_SHARDING_COUNT_KEY_NAME = "table-sharding-count";
    
    private int shardingCount;
    
    private int tableShardingCount;
    
    @Override
    public void init(Properties props) {
        this.shardingCount = Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY_NAME));
        this.tableShardingCount = Integer.parseInt(props.getProperty(TABLE_SHARDING_COUNT_KEY_NAME));
    }
    @Override
    public Collection<String> doSharding(Collection<String> allActualSplitDatabaseNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        List<String> actualDatabaseNames = new ArrayList<>(allActualSplitDatabaseNames.size());
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        if (CollectionUtil.isEmpty(columnNameAndShardingValuesMap)) {
            return actualDatabaseNames;
        }
        
        Collection<Long> orderNumberValues = columnNameAndShardingValuesMap.get("order_number");
        Collection<Long> userIdValues = columnNameAndShardingValuesMap.get("user_id");
        
        Long value = null;
        if (CollectionUtil.isNotEmpty(orderNumberValues)) {
            value = orderNumberValues.stream().findFirst().orElseThrow(() -> new DaMaiFrameException(BaseCode.ORDER_NUMBER_NOT_EXIST));
        } else if (CollectionUtil.isNotEmpty(userIdValues)) {
            value = userIdValues.stream().findFirst().orElseThrow(() -> new DaMaiFrameException(BaseCode.USER_ID_NOT_EXIST));
        }
        if (Objects.nonNull(value)) {
            long tableIndex = value % tableShardingCount;
            long databaseIndex = calculateDatabaseIndex(shardingCount,tableIndex,tableShardingCount);
            String databaseIndexStr = String.valueOf(databaseIndex);
            for (String actualSplitDatabaseName : allActualSplitDatabaseNames) {
                if (actualSplitDatabaseName.contains(databaseIndexStr)) {
                    actualDatabaseNames.add(actualSplitDatabaseName);
                    break;
                }
            }
            return actualDatabaseNames;
        }else {
            return allActualSplitDatabaseNames;
        }
    }
    
    /**
     * 计算给定表索引应分配到的数据库编号。
     *
     * @param databaseCount 数据库总数
     * @param tableIndex    表索引
     * @param tableCount    表总数
     * @return 分配到的数据库编号
     */
    public long calculateDatabaseIndex(long databaseCount, long tableIndex, long tableCount) {
        long tablesPerDatabase = tableCount / databaseCount;
        long remainder = tableCount % databaseCount;
        
        long databaseIndex;
        if (tableIndex < (tablesPerDatabase + 1) * remainder) {
            databaseIndex = tableIndex / (tablesPerDatabase + 1);
        } else {
            databaseIndex = remainder + (tableIndex - (tablesPerDatabase + 1) * remainder) / tablesPerDatabase;
        }
        
        return databaseIndex;
    }
}
