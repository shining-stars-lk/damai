//package com.example.mapper;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.example.entity.WorkerNode;
//import org.apache.ibatis.annotations.Param;
//
///**
// * <p>
// * DB WorkerID Assigner for UID Generator Mapper 接口
// * </p>
// *
// * @author 星哥
// * @since 2023-05-16
// */
//public interface WorkerNodeMapper extends BaseMapper<WorkerNode> {
//    /**
//     * Get {@link WorkerNode} by node host
//     *
//     * @param host
//     * @param port
//     * @return
//     */
//    WorkerNode getWorkerNodeByHostPort(@Param("host") String host, @Param("port") String port);
//
//    /**
//     * Add {@link WorkerNode}
//     *
//     * @param workerNodeEntity
//     */
//    void addWorkerNode(WorkerNode workerNodeEntity);
//
//}