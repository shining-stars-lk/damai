package com.example.composite;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeInterface<T> {
    
    protected List<CompositeInterface> list = new ArrayList<>();
    
    /**
     * 执行业务
     * */
    protected abstract void execute(T param);
    
    public abstract String type();
    
    public abstract Integer executeTier();
    
    public abstract Integer executeOrder();
     
     public void add(CompositeInterface compositeInterface) {
         list.add(compositeInterface);
     }
     
     public void batch(T param) {
         execute(param);
         for (CompositeInterface compositeInterface : list) {
             compositeInterface.batch(param);
         }
     }
}
