package com.example.servicelock.info;


/**
 * @program: redis-example
 * @description: 分布式锁失败处理接口实现类
 * @author: 星哥
 * @create: 2023-05-28
 **/
public enum LockTimeOutStrategy implements LockTimeOutHandler{

    FAIL(){
        @Override
        public void handler(String lockName) {
            String msg = String.format("%s请求频繁",lockName);
            throw new RuntimeException(msg);
        }
    }
}
