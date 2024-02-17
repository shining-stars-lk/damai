package com.damai.handler;

import com.damai.save.DataSave;
import com.damai.structure.MethodHierarchyTransfer;
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
