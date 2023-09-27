package com.example.service;


import com.example.model.CpuInfo;
import com.example.model.HeapMemoryInfo;
import com.example.model.PhysicalMemoryInfo;
import com.example.util.Context;
import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SysUsageService {
    private static Logger log = Logger.getLogger(SysUsageService.class.toString());

    public static SysUsageService newInstance() {
        return new SysUsageService();
    }

    public CpuInfo getCpuInfo() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                - prevTicks[CentralProcessor.TickType.NICE.getIndex()];

        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];

        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];

        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];

        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];

        long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                - prevTicks[CentralProcessor.TickType.USER.getIndex()];

        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];

        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];

        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setLogicalNum(processor.getLogicalProcessorCount());
        cpuInfo.setUserRate(user * 1.0 / totalCpu);
        cpuInfo.setSysRate(cSys * 1.0 / totalCpu);
        cpuInfo.setWaitRate(iowait * 1.0 / totalCpu);
        cpuInfo.setSystemLoad(processor.getSystemCpuLoad(1000));
        return cpuInfo;
    }

    public HeapMemoryInfo getHeapMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        long initTotalMemorySize = memoryUsage.getInit();
        long maxMemorySize = memoryUsage.getMax();
        long usedMemorySize = memoryUsage.getUsed();
        HeapMemoryInfo heapMemoryInfo = new HeapMemoryInfo();
        heapMemoryInfo.setInitValue(initTotalMemorySize);
        heapMemoryInfo.setMaxValue(maxMemorySize);
        heapMemoryInfo.setUsedValue(usedMemorySize);
        heapMemoryInfo.setUsedRate(usedMemorySize * 1.0 / maxMemorySize);
        return heapMemoryInfo;
    }

    public PhysicalMemoryInfo getPhysicalMemoryInfo() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        PhysicalMemoryInfo physicalMemoryInfo = new PhysicalMemoryInfo();
        physicalMemoryInfo.setInitValue(osmxb.getTotalPhysicalMemorySize());
        physicalMemoryInfo.setUsedValue(osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());
        physicalMemoryInfo.setFreeValue(osmxb.getFreePhysicalMemorySize());
        physicalMemoryInfo.setUsedValue(physicalMemoryInfo.getInitValue() - physicalMemoryInfo.getFreeValue());
        physicalMemoryInfo.setUsedRate(physicalMemoryInfo.getUsedValue() * 1.0 / physicalMemoryInfo.getInitValue());
        if (isLinux()) {
            Map<String, String> processInfo = getProcessInfo();
            if (processInfo.containsKey("VmSize")) {
                String VmRSSStr = processInfo.get("VmRSS");
                String VmSizeStr = VmRSSStr.split(" ")[0].trim();
                long VmRSS = Long.valueOf(VmSizeStr);
                physicalMemoryInfo.setThisUsedValue(VmRSS*1024);
                double rate = physicalMemoryInfo.getThisUsedValue()*1.0 / physicalMemoryInfo.getInitValue();
                physicalMemoryInfo.setThisUsedRate(rate);
            }
        }
        return physicalMemoryInfo;
    }

    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public Map<String,String> getProcessInfo() {
        Map<String,String> processMetrics = new HashMap();
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec("cat /proc/" + Context.getPid() + "/status");
        } catch (IOException e) {
            log.severe("Can not execute '"+"cat /proc/" + Context.getPid() + "/status"+"'");
            return processMetrics;
        }
        try (InputStream inputStream = process.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            String line ="";
            while ((line = bufferedReader.readLine()) != null){
                String[] split = line.split(":");
                if (split.length==2) {
                    String key = split[0].trim();
                    String value = split[1].trim();
                    processMetrics.put(key,value);
                }
            }
        } catch (Exception e) {
            log.severe("Can not read the result of '"+"cat /proc/" + Context.getPid() + "/status"+"'");
        }
        return processMetrics;
    }

}

