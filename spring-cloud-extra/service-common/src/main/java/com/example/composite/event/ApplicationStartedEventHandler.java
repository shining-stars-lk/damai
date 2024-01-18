package com.example.composite.event;

import com.example.composite.CompositeContainer;
import com.example.composite.CompositeInterface;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * ApplicationStartedEventHandler 类用于处理应用程序启动事件。
 */
@AllArgsConstructor
public class ApplicationStartedEventHandler implements ApplicationListener<ApplicationStartedEvent> {
    
    private final CompositeContainer compositeContainer;
    
    
    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        // 获取所有 CompositeInterface 类型的 Bean
        Map<String, CompositeInterface> compositeInterfaceMap = event.getApplicationContext().getBeansOfType(CompositeInterface.class);
        
        // 构建组件树结构
        CompositeInterface root = buildMenu(compositeInterfaceMap.values());
        
        // 如果根节点存在，则执行业务逻辑
        if (root != null) {
            root.executeByLevel(null);
        }
    }
    
    /**
     * 构建组件树的辅助方法。
     * @param groupedByTier 按层级组织的组件映射。
     * @param currentTier 当前处理的层级。
     */
    private static void buildTree(Map<Integer, Map<Integer, CompositeInterface>> groupedByTier, int currentTier) {
        Map<Integer, CompositeInterface> currentLevelComponents = groupedByTier.get(currentTier);
        Map<Integer, CompositeInterface> nextLevelComponents = groupedByTier.get(currentTier + 1);
        
        if (currentLevelComponents == null) {
            return; // 当前层级没有组件时，直接返回
        }
        
        if (nextLevelComponents != null) {
            for (CompositeInterface child : nextLevelComponents.values()) {
                Integer parentOrder = child.executeParentOrder();
                if (parentOrder == null || parentOrder == 0) {
                    continue; // 跳过根节点
                }
                CompositeInterface parent = currentLevelComponents.get(parentOrder);
                if (parent != null) {
                    parent.add(child); // 将子节点添加到父节点的子列表中
                }
            }
        }
        
        // 递归构建下一层级的树结构
        buildTree(groupedByTier, currentTier + 1);
    }
    
    /**
     * 根据提供的组件集合构建组件树，并返回根节点。
     * @param components 组件集合。
     * @return 根节点。
     */
    public static CompositeInterface buildMenu(Collection<CompositeInterface> components) {
        // 按层级和执行顺序组织组件
        Map<Integer, Map<Integer, CompositeInterface>> groupedByTier = new TreeMap<>();
        
        for (CompositeInterface component : components) {
            groupedByTier.computeIfAbsent(component.executeTier(), k -> new HashMap<>())
                    .put(component.executeOrder(), component); // 使用 executeOrder 作为键
        }
        
        // 找到最小层级
        Integer minTier = groupedByTier.keySet().stream().min(Integer::compare).orElse(null);
        if (minTier == null) {
            return null; // 没有组件时返回空
        }
        
        // 构建组件树
        buildTree(groupedByTier, minTier);
        
        // 找到并返回根节点
        return groupedByTier.get(minTier).values().stream()
                .filter(c -> c.executeParentOrder() == null || c.executeParentOrder() == 0)
                .findFirst()
                .orElse(null);
    }
    
}
