package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-09
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    private Integer age;
    
    private String name;
}
