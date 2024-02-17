package com.damai.pay;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
@Data
@AllArgsConstructor
public class PayResult {
    
    private final boolean success;
    
    private final String body;
}
