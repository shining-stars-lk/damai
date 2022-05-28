package com.example.distributecache.distributedid;

import com.example.distributecache.core.CacheKeyEnum;
import com.example.distributecache.core.DistributedLockConstants;
import com.example.distributecache.core.StringUtil;
import com.example.distributecache.distributedlock.util.DistributedLockUtil;
import com.example.distributecache.distributedlock.util.TaskCall;
import com.example.distributecache.redis.CacheKeyWrap;
import com.example.distributecache.redis.DistributCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 获取id信息类
 * @author: lk
 * @create: 2022-05-28
 **/
@Component
public class IdGeneratorProvider {

    // 自增下限
    private static final int SMALL_VALUE = 1;
    // 自增上限
    private static final int BIG_VALUE = 999;
    private static final String PADDING_ZERO_TWO = "00";
    private static final String PADDING_ZERO_ONE = "0";

    @Autowired
    private DistributCache distributCache;

    @Autowired
    private DistributedLockUtil distributedLockUtil;

    public String lockGetDatacenterId(String currentTimeMillis){
        TaskCall<String> taskCall = () -> getDatacenterId(currentTimeMillis);
        String[] keys = new String[1];
        keys[0] = String.valueOf(currentTimeMillis);
        String result = distributedLockUtil.submit(taskCall, DistributedLockConstants.DATACENTER_Id, keys);
        return result;
    }

    public String getDatacenterId(String currentTimeMillis){
        String result = null;

        CacheKeyWrap cacheKeyWrap = CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.DISTRIBUTED_DATACENTER_ID, String.valueOf(currentTimeMillis));

        Integer datacenterId = distributCache.get(cacheKeyWrap, Integer.class);
        if (datacenterId == null) {
            //设置过期时间,到期后过期的毫秒清除掉
            distributCache.set(cacheKeyWrap,SMALL_VALUE,10, TimeUnit.SECONDS);
            result = String.valueOf(SMALL_VALUE);
        }else {
            // 如果小于上限值，则本周期内继续做累加
            if (datacenterId <= BIG_VALUE) {
                datacenterId++;
                //设置过期时间,到期后过期的毫秒清除掉
                distributCache.set(cacheKeyWrap,datacenterId,10, TimeUnit.SECONDS);
                result = String.valueOf(datacenterId);
            }
        }
        if (StringUtil.isNotEmpty(result)) {
            int resultInt = Integer.parseInt(result);
            if (resultInt < 10) {
                result = new StringBuilder().append(PADDING_ZERO_TWO).append(result).toString();
            } else if (resultInt < 100) {
                result = new StringBuilder().append(PADDING_ZERO_ONE).append(result).toString();
            }
        }
        return result;
    }
}
