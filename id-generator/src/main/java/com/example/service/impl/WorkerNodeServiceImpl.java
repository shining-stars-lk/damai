package com.example.service.impl;

import com.baidu.fsg.uid.UidGenerator;
import com.example.service.IWorkerNodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * DB WorkerID Assigner for UID Generator 服务实现类
 * </p>
 *
 * @author huzhiting
 * @since 2020-04-23
 */
@Service
public class WorkerNodeServiceImpl implements IWorkerNodeService {

    @Resource
    private UidGenerator uidGenerator;

    @Override
    public long genUid() {
        return uidGenerator.getUID();
    }
}