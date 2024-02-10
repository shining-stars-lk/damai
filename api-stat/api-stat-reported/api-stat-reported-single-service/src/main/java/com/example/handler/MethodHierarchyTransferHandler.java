package com.example.handler;

import com.example.core.RedisKeyEnum;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.save.DataSave;
import com.example.structure.MethodHierarchyTransfer;
import lombok.AllArgsConstructor;

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
