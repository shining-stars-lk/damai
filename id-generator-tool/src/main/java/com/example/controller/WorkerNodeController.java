package com.example.controller;


import com.example.service.IWorkerNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * DB WorkerID Assigner for UID Generator 前端控制器
 * </p>
 *
 * @author huzhiting
 * @since 2020-04-23
 */
@RestController
@RequestMapping("/worker-node")
public class WorkerNodeController {
    
    @Autowired
    private IWorkerNodeService iWorkerNodeService;
    
    @RequestMapping("/genUid")
    public Long genUid(){
        return iWorkerNodeService.genUid();
    }

}