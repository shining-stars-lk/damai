package com.damai.notice.config;

import com.damai.enums.PlatformType;
import com.damai.exception.DaMaiFrameException;
import com.damai.notice.Platform;
import com.damai.notice.impl.DingDingPlatform;
import com.damai.notice.impl.EmailPlatform;
import com.damai.notice.impl.FeiShuPlatform;
import com.damai.notice.impl.WeComPlatform;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.damai.enums.BaseCode.MESSAGE_PLATFORM_NOT_EXIST;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-07
 **/
@AllArgsConstructor
public class PlatformRegistry {
    
    private final ApiStatNoticeProperties apiStatNoticeProperties;
    
    public List<Platform> createPlatforms(){
        List<PlatformType> platformTypes = apiStatNoticeProperties.getPlatformTypes();
        if (CollectionUtils.isEmpty(platformTypes)) {
            return null;
        }
        List<Platform> list = new ArrayList<>();
        for (final PlatformType platformType : platformTypes) {
            list.add(createPlatform(platformType,apiStatNoticeProperties));
        }
        return list;
    }
    
    public Platform createPlatform(PlatformType platformType,ApiStatNoticeProperties apiStatNoticeProperties){
        switch (platformType) {
            case DingDing:
                return new DingDingPlatform(apiStatNoticeProperties);
            case WeCom:
                return new WeComPlatform(apiStatNoticeProperties);
            case FeiShu:
                return new FeiShuPlatform(apiStatNoticeProperties);
            case Email:
                return new EmailPlatform(apiStatNoticeProperties);
            default:
                throw new DaMaiFrameException(MESSAGE_PLATFORM_NOT_EXIST);    
        }
    }
}
