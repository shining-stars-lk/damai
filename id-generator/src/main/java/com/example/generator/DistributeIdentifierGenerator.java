package com.example.generator;

import java.net.InetAddress;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-25
 **/
public class DistributeIdentifierGenerator implements IdentifierGenerator {
    
    private final Sequence sequence;
    
    public DistributeIdentifierGenerator() {
        this.sequence = new Sequence(null);
    }
    
    public DistributeIdentifierGenerator(InetAddress inetAddress) {
        this.sequence = new Sequence(inetAddress);
    }
    
    public DistributeIdentifierGenerator(long workerId, long dataCenterId) {
        this.sequence = new Sequence(workerId, dataCenterId);
    }
    
    public DistributeIdentifierGenerator(Sequence sequence) {
        this.sequence = sequence;
    }
    
    @Override
    public Long nextId() {
        return sequence.nextId();
    }
}
