package com.tool.servicelock.info;


/**
 * @program: redis-tool
 * @description: 分布式锁失败处理接口实现类
 * @author: kuan
 * @create: 2023-05-28
 **/
public enum LockTimeOutStrategy implements LockTimeOutHandler{

    FAIL(){
        @Override
        public void handler(String lockName) {
            String msg = String.format("请求频繁",lockName);
            throw new RuntimeException(msg);
        }
    }
}
