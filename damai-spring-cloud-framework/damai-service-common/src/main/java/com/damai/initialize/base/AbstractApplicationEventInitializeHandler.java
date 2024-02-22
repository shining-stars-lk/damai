package com.damai.initialize.base;

import static com.damai.initialize.constant.InitializeHandlerType.APPLICATION_EVENT;

public abstract class AbstractApplicationEventInitializeHandler implements InitializeHandler {
    
    @Override
    public String type() {
        return APPLICATION_EVENT;
    }
}
