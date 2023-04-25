package com.example.generator;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-25
 **/
public interface IdentifierGenerator {
    
    /**
     * 生成Id
     *
     * @param entity 实体
     * @return id
     */
    Number nextId();
}
