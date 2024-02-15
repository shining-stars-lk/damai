package com.example.shardingsphere;

import cn.hutool.core.collection.CollectionUtil;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class DatabaseOrderComplexGeneArithmetic implements ComplexKeysShardingAlgorithm<Long> {
    /**
     * 属性分库名
     * */
    private static final String SHARDING_COUNT_KEY_NAME = "sharding-count";
    
    /**
     * 属性分库名
     * */
    private static final String TABLE_SHARDING_COUNT_KEY_NAME = "table-sharding-count";
    
    /**
     * 分库数量
     * */
    private int shardingCount;
    
    /**
     * 分表数量
     * */
    private int tableShardingCount;
    
    @Override
    public void init(Properties props) {
        this.shardingCount = Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY_NAME));
        this.tableShardingCount = Integer.parseInt(props.getProperty(TABLE_SHARDING_COUNT_KEY_NAME));
    }
    @Override
    public Collection<String> doSharding(Collection<String> allActualSplitDatabaseNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        //返回的真实库名集合
        List<String> actualDatabaseNames = new ArrayList<>(allActualSplitDatabaseNames.size());
        //查询中的列名和值
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        //如果没有条件查询，那么就查所有的分表
        if (CollectionUtil.isEmpty(columnNameAndShardingValuesMap)) {
            return actualDatabaseNames;
        }
        
        Collection<Long> orderNumberValues = columnNameAndShardingValuesMap.get("order_number");
        
        Collection<Long> userIdValues = columnNameAndShardingValuesMap.get("user_id");
        
        Long value = null;
        //如果是order_number查询
        if (CollectionUtil.isNotEmpty(orderNumberValues)) {
            value = orderNumberValues.stream().findFirst().orElseThrow(() -> new CookFrameException(BaseCode.ORDER_NUMBER_NOT_EXIST));
            //如果是user_id查询
        } else if (CollectionUtil.isNotEmpty(userIdValues)) {
            value = orderNumberValues.stream().findFirst().orElseThrow(() -> new CookFrameException(BaseCode.USER_ID_NOT_EXIST));
        }
        //如果order_number或者user_id的值存在
        if (Objects.nonNull(value)) {
            //这里和分表数量进行取模，是因为订单编号中在插入user_id的基因时，是和表数量进行取模的
            long tableIndex = value % tableShardingCount;
            //获得值后再获得实际的分库的索引
            long databaseIndex = calculateDatabaseIndex(shardingCount,tableIndex,tableShardingCount);
            String databaseIndexStr = String.valueOf(databaseIndex);
            for (String actualSplitDatabaseName : allActualSplitDatabaseNames) {
                //将所有的分库名和得到的分库索引进行匹配
                if (actualSplitDatabaseName.contains(databaseIndexStr)) {
                    actualDatabaseNames.add(actualSplitDatabaseName);
                    break;
                }
            }
        }
        return actualDatabaseNames;
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
        // 每个数据库平均分配到的表数量（向下取整）
        long tablesPerDatabase = tableCount / databaseCount;
        // 不能均匀分配的表的数量
        long remainder = tableCount % databaseCount;
        
        // 计算当前tableIndex应分配到的数据库编号
        long databaseIndex;
        if (tableIndex < (tablesPerDatabase + 1) * remainder) {
            // 如果tableIndex在能均匀分配的表的范围内
            databaseIndex = tableIndex / (tablesPerDatabase + 1);
        } else {
            // 如果tableIndex在不能均匀分配的表的范围内
            databaseIndex = remainder + (tableIndex - (tablesPerDatabase + 1) * remainder) / tablesPerDatabase;
        }
        
        return databaseIndex;
    }
}
