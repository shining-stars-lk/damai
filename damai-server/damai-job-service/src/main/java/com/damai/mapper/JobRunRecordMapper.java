package com.damai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damai.entity.JobRunRecord;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: job记录 mapper
 * @author: 阿宽不是程序员
 **/
public interface JobRunRecordMapper extends BaseMapper<JobRunRecord> {
    
    /**
     * 上报日志状态
     * @param jobRunRecord 数据
     * @return 结果
     * */
    int callBack(JobRunRecord jobRunRecord);
}
