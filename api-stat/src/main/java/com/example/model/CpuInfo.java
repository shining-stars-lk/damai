package com.example.model;

public class CpuInfo {
    private Double systemLoad;
    private Double userRate;
    private Double sysRate;
    private Double waitRate;
    private Integer logicalNum;

    public Double getWaitRate() {
        return waitRate;
    }

    public void setWaitRate(Double waitRate) {
        this.waitRate = waitRate;
    }

    public Double getSystemLoad() {
        return systemLoad;
    }

    public void setSystemLoad(Double systemLoad) {
        this.systemLoad = systemLoad;
    }

    public Double getUserRate() {
        return userRate;
    }

    public void setUserRate(Double userRate) {
        this.userRate = userRate;
    }

    public Double getSysRate() {
        return sysRate;
    }

    public void setSysRate(Double sysRate) {
        this.sysRate = sysRate;
    }

    public Integer getLogicalNum() {
        return logicalNum;
    }

    public void setLogicalNum(Integer logicalNum) {
        this.logicalNum = logicalNum;
    }
}
