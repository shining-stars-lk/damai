package com.example.rel.operate;


import com.example.rel.handler.MethodHierarchyTransferHandler;
import com.example.rel.structure.MethodHierarchyTransfer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class MethodQueueOperate {

    private final BlockingQueue<MethodHierarchyTransfer> blockingQueue = new ArrayBlockingQueue<>(5000);
    
    private final MethodHierarchyTransferHandler methodHierarchyTransferHandler;
    
    public MethodQueueOperate(MethodHierarchyTransferHandler methodHierarchyTransferHandler){
        this.methodHierarchyTransferHandler = methodHierarchyTransferHandler;
    }

    public void add(MethodHierarchyTransfer methodHierarchyTransfer) {
        blockingQueue.add(methodHierarchyTransfer);
    }
    

    public void takeTask() {
        for(;;) {
            try {
                MethodHierarchyTransfer methodHierarchyTransfer = blockingQueue.take();
                methodHierarchyTransferHandler.consumer(methodHierarchyTransfer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
