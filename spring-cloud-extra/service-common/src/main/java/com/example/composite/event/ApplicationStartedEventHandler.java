package com.example.composite.event;

import com.example.composite.CompositeContainer;
import com.example.composite.CompositeInterface;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ApplicationStartedEventHandler implements ApplicationListener<ApplicationStartedEvent> {
    
    private final CompositeContainer compositeContainer;
    
    
    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        Map<String, CompositeInterface> compositeInterfaceMap = event.getApplicationContext().getBeansOfType(CompositeInterface.class);
        
        
        Map<Integer, List<CompositeInterface>> map = compositeInterfaceMap.values()
                .stream()
                .sorted(Comparator.comparingInt(CompositeInterface::executeOrder))
                .collect(Collectors.groupingBy(CompositeInterface::executeTier, TreeMap::new, Collectors.toList()));
        // 找到最小层级
        Integer minTier = map.keySet().stream().min(Integer::compare).orElseThrow(() -> new CookFrameException(BaseCode.COMPOSITE_NOT_EXIST));
        // 构建菜单树
        CompositeInterface root = buildTree(map, minTier);
        
        compositeContainer.add("1",root);
    }
    
    private CompositeInterface buildTree(Map<Integer, List<CompositeInterface>> groupedByTier, int currentTier) {
        List<CompositeInterface> currentLevelComponents = groupedByTier.get(currentTier);
        
        // 如果当前层级没有组件，则返回 null
        if (currentLevelComponents == null) {
            return null;
        }
        
        CompositeInterface root = null;
        
        for (CompositeInterface component : currentLevelComponents) {
            // 递归构建子组件的树结构
            CompositeInterface subTree = buildTree(groupedByTier, currentTier + 1);
            
            // 在第一个元素时设置根节点
            if (root == null) {
                root = component;
            }
            
            // 将构建的子树添加到当前组件下
            if (subTree != null) {
                component.add(subTree);
            }
        }
        
        return root;
    }
    
}
