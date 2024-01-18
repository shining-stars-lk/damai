package com.example.composite.event;

import com.example.composite.CompositeContainer;
import com.example.composite.CompositeInterface;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
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
        // 构建菜单树
        CompositeInterface root = buildMenu(compositeInterfaceMap.values());
        
        root.executeByLevel(null);
        compositeContainer.add("1",root);
    }
    
    private static void buildTree(Map<Integer, Map<Integer, CompositeInterface>> groupedByTier, int currentTier) {
        Map<Integer, CompositeInterface> currentLevelComponents = groupedByTier.get(currentTier);
        Map<Integer, CompositeInterface> nextLevelComponents = groupedByTier.get(currentTier + 1);
        
        if (currentLevelComponents == null) {
            return;
        }
        
        if (nextLevelComponents != null) {
            for (CompositeInterface child : nextLevelComponents.values()) {
                Integer parentOrder = child.executeParentOrder();
                if (parentOrder == null || parentOrder == 0) {
                    continue; // 跳过根节点
                }
                CompositeInterface parent = currentLevelComponents.get(parentOrder);
                if (parent != null) {
                    parent.add(child);
                }
            }
        }
        
        // 递归构建下一层级的树结构
        buildTree(groupedByTier, currentTier + 1);
    }
    
    public static CompositeInterface buildMenu(Collection<CompositeInterface> components) {
        Map<Integer, Map<Integer, CompositeInterface>> groupedByTier = new TreeMap<>();
        
        for (CompositeInterface component : components) {
            groupedByTier.computeIfAbsent(component.executeTier(), k -> new HashMap<>())
                    .put(component.executeOrder(), component); // 使用 executeOrder 作为键
        }
        
        Integer minTier = groupedByTier.keySet().stream().min(Integer::compare).orElse(null);
        if (minTier == null) {
            return null; // 没有组件时返回空
        }
        
        // 构建树
        buildTree(groupedByTier, minTier);
        
        // 找到根节点
        return groupedByTier.get(minTier).values().stream()
                .filter(c -> c.executeParentOrder() == null || c.executeParentOrder() == 0)
                .findFirst()
                .orElse(null);
    }
    
}
