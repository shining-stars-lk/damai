package com.example.model;

public class HeapMemoryInfo {
    private Long initValue;
    private Long maxValue;
    private Long usedValue;
    private Double usedRate;

    public Long getInitValue() {
        return initValue;
    }

    public void setInitValue(Long initValue) {
        this.initValue = initValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
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
