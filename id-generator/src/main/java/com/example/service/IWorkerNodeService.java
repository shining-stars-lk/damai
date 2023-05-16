package com.example.service;

import com.example.entity.WorkerNode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * DB WorkerID Assigner for UID Generator 服务类
 * </p>
 *
 * @author huzhiting
 * @since 2020-04-23
 */
public interface IWorkerNodeService extends IService<WorkerNode> {

    long genUid();

}