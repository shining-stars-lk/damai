package com.example.model;

public class MethodRelation {
    private String id;
    private String sourceId;
    private String targetId;
    private Double avgRunTime = 0.0;
    private Double maxRunTime = 0.0;
    private Double minRunTime = 10000.0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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


}
