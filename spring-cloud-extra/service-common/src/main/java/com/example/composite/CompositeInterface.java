package com.example.composite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
/**
 * 抽象类 CompositeInterface 表示组合接口，用于构建和执行具有层级结构的业务逻辑。
 * @param <T> 泛型参数，表示执行业务时传递的参数类型。
 */
public abstract class CompositeInterface<T> {
    /**
     * 存储子节点的列表
     * 
     */
    
    protected List<CompositeInterface<T>> list = new ArrayList<>(); 
    
    /**
     * 执行具体业务的抽象方法，由子类具体实现。
     * @param param 泛型参数，用于业务执行。
     */
    protected abstract void execute(T param);
    
    /**
     * @return 返回组件的类型。
     */
    public abstract String type();
    
    /**
     * @return 返回父级执行顺序，用于建立层级关系.(根节点的话返回值为0)
     */
    public abstract Integer executeParentOrder();
    
    /**
     * @return 返回组件的执行层级。
     */
    public abstract Integer executeTier();
    
    /**
     * @return 返回组件在同一层级中的执行顺序。
     */
    public abstract Integer executeOrder();
    
    /**
     * 将子组件添加到当前组件的子列表中。
     * @param compositeInterface 子组件实例。
     */
    public void add(CompositeInterface<T> compositeInterface) {
        list.add(compositeInterface);
    }
    
    /**
     * 按层次结构执行每个组件的业务逻辑。
     * @param param 泛型参数，用于业务执行。
     */
    public void executeByLevel(T param) {
        Queue<CompositeInterface<T>> queue = new LinkedList<>();
        // 将当前对象加入队列
        queue.add(this); 
        
        while (!queue.isEmpty()) {
            // 当前层的大小
            int levelSize = queue.size(); 
            
            for (int i = 0; i < levelSize; i++) {
                // 从队列中取出一个元素
                CompositeInterface<T> current = queue.poll(); 
                
                // 执行当前元素的业务逻辑
                current.execute(param);
                
                // 将当前元素的子元素加入队列，以便在下一次迭代中处理
                queue.addAll(current.list);
            }
        }
    }
}
