package com.example.handler;

import com.example.config.ApiStatProperties;
import com.example.core.RedisKeyEnum;
import com.example.core.StringUtil;
import com.example.enums.MethodLevel;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.save.DataSave;
import com.example.structure.MethodData;
import com.example.structure.MethodDetailData;
import com.example.structure.MethodHierarchyTransfer;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@AllArgsConstructor
public class MethodHierarchyTransferHandler{
    
    private final DataSave dataSave;

    public void consumer(MethodHierarchyTransfer methodHierarchyTransfer){
        dataSave.save(methodHierarchyTransfer);
    }
}
