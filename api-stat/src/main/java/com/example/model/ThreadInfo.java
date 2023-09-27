package com.example.model;

import java.util.List;

public class ThreadInfo {
    private Long id;
    private String name;
    private String classType;
    private String state;
    private Boolean isInterrupted;
    private Boolean isDaemon;
    private Integer priority;
    private List<StackTraceElement> stacks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getInterrupted() {
        return isInterrupted;
    }

    public void setInterrupted(Boolean interrupted) {
        isInterrupted = interrupted;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getDaemon() {
        return isDaemon;
    }

    public void setDaemon(Boolean daemon) {
        isDaemon = daemon;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<StackTraceElement> getStacks() {
        return stacks;
    }

    public void setStacks(List<StackTraceElement> stacks) {
        this.stacks = stacks;
    }
}
