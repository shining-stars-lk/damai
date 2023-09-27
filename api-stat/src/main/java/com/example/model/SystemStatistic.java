package com.example.model;


public class SystemStatistic {
    private String name;
    private Double avgRunTime = 0.0;
    private Double maxRunTime = 0.0;
    private Double minRunTime = 0.0;
    private Integer totalNum = 0 ;
    private Integer delayNum = 0;
    private Integer normalNum = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAvgRunTime() {
        return avgRunTime;
    }

    public void setAvgRunTime(Double avgRunTime) {
        this.avgRunTime = avgRunTime;
    }

    public Double getMaxRunTime() {
        return maxRunTime;
    }

    public void setMaxRunTime(Double maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    public Double getMinRunTime() {
        return minRunTime;
    }

    public void setMinRunTime(Double minRunTime) {
        this.minRunTime = minRunTime;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getDelayNum() {
        return delayNum;
    }

    public void setDelayNum(Integer delayNum) {
        this.delayNum = delayNum;
    }

    public Integer getNormalNum() {
        return normalNum;
    }

    public void setNormalNum(Integer normalNum) {
        this.normalNum = normalNum;
    }

    @Override
    public String toString() {
        return "SystemStatistic{" +
                "name='" + name + '\'' +
                ", avgRunTime=" + avgRunTime +
                ", maxRunTime=" + maxRunTime +
                ", minRunTime=" + minRunTime +
                ", totalNum=" + totalNum +
                ", delayNum=" + delayNum +
                ", normalNum=" + normalNum +
                '}';
    }
}
