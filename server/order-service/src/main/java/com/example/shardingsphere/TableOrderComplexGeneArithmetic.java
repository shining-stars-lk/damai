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

public class TableOrderComplexGeneArithmetic implements ComplexKeysShardingAlgorithm<Long> {
    
    /**
     * 属性分表名
     * */
    private static final String SHARDING_COUNT_KEY_NAME = "sharding-count";
    /**
     * 分表数量
     * */
    private int shardingCount;
    
    @Override
    public void init(Properties props) {
        shardingCount = Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY_NAME));
    }
    @Override
    public Collection<String> doSharding(Collection<String> allActualSplitTableNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        //返回的真实表名集合
        List<String> actualTableNames = new ArrayList<>(allActualSplitTableNames.size());
        //逻辑表名
        String logicTableName = complexKeysShardingValue.getLogicTableName();
        //查询中的列名和值
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        //如果没有条件查询，那么就查所有的分表
        if (CollectionUtil.isEmpty(columnNameAndShardingValuesMap)) {
            return actualTableNames;
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
            actualTableNames.add(logicTableName + "_" + value % shardingCount);
        }
        return actualTableNames;
    }
}
