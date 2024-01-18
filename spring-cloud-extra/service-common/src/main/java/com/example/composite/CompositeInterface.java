package com.example.composite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class CompositeInterface<T> {
    
    protected List<CompositeInterface<T>> list = new ArrayList<>();
    
    /**
     * 执行业务
     * */
    protected abstract void execute(T param);
    
    public abstract String type();
    
    public abstract Integer executeParentOrder();
    
    public abstract Integer executeTier();
    
    public abstract Integer executeOrder();
     
     public void add(CompositeInterface<T> compositeInterface) {
         list.add(compositeInterface);
     }
    
    /**
     * 按层次执行方法
     */
    public void executeByLevel(T param) {
        Queue<CompositeInterface<T>> queue = new LinkedList<>();
        queue.add(this); // 将当前对象加入队列
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // 当前层的大小
            
            for (int i = 0; i < levelSize; i++) {
                CompositeInterface<T> current = queue.poll(); // 从队列中取出一个元素
                
                // 执行当前元素的业务逻辑
                current.execute(param);
                
                // 将当前元素的子元素加入队列
                queue.addAll(current.list);
            }
        }
    }
}
