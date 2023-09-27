package com.example.model;

public class PhysicalMemoryInfo {
    private Long initValue;
    private Long freeValue;
    private Long usedValue;
    private Double usedRate;
    private Long thisUsedValue;
    private Double thisUsedRate;
    private Long thisUsedPeak;

    public Long getThisUsedPeak() {
        return thisUsedPeak;
    }

    public void setThisUsedPeak(Long thisUsedPeak) {
        this.thisUsedPeak = thisUsedPeak;
    }

    public Long getThisUsedValue() {
        return thisUsedValue;
    }

    public void setThisUsedValue(Long thisUsedValue) {
        this.thisUsedValue = thisUsedValue;
    }

    public Double getThisUsedRate() {
        return thisUsedRate;
    }

    public void setThisUsedRate(Double thisUsedRate) {
        this.thisUsedRate = thisUsedRate;
    }

    public Long getInitValue() {
        return initValue;
    }

    public void setInitValue(Long initValue) {
        this.initValue = initValue;
    }

    public Long getFreeValue() {
        return freeValue;
    }

    public void setFreeValue(Long freeValue) {
        this.freeValue = freeValue;
    }

    public Long getUsedValue() {
        return usedValue;
    }

    public void setUsedValue(Long usedValue) {
        this.usedValue = usedValue;
    }

    public Double getUsedRate() {
        return usedRate;
    }

    public void setUsedRate(Double usedRate) {
        this.usedRate = usedRate;
    }
}
