package com.mqunar.necro.agent.sample;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.sample.Sample.SampleType;
import com.mqunar.necro.agent.tracing.ActivityTrace;
import com.mqunar.necro.agent.tracing.ITraceLifecycleAware;
import com.mqunar.necro.agent.tracing.TraceMachine;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Sampler implements ITraceLifecycleAware {
    private static final int KB_IN_MB = 1024;
    private static final int[] PID = new int[]{Process.myPid()};
    private static boolean cpuSamplingDisabled = false;
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private static Sampler sampler;
    private static final ReentrantLock samplerLock = new ReentrantLock();
    private final ActivityManager activityManager;
    private RandomAccessFile appStatFile;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private Long lastAppCpuTime;
    private Long lastCpuTime;
    private RandomAccessFile procStatFile;
    private final EnumMap<SampleType, Collection<Sample>> samples = new EnumMap(SampleType.class);

    private Sampler(Context context) {
        this.activityManager = (ActivityManager) context.getSystemService("activity");
        this.samples.put(SampleType.MEMORY, new ArrayList());
        this.samples.put(SampleType.CPU, new ArrayList());
    }

    public static void init(Context context) {
        samplerLock.lock();
        sampler = new Sampler(context);
        samplerLock.unlock();
        TraceMachine.addTraceListener(sampler);
        log.debug("Sampler Initialized");
    }

    public void onEnterMethod() {
        log.debug("Sampler onEnterMethod");
        if (!this.isRunning.get()) {
            start();
        }
    }

    public void onExitMethod() {
        log.debug("Sampler onExitMethod");
    }

    public void onTraceStart(ActivityTrace activityTrace) {
        log.debug("Sampler onTraceStart");
        start();
    }

    public void onTraceComplete(ActivityTrace activityTrace) {
        log.debug("Sampler onTraceComplete");
        stop();
        activityTrace.setVitals(copySamples());
        clear();
    }

    private void sample() {
        log.debug("Sampler sample");
        samplerLock.lock();
        try {
            Sample sampleMemory = sampleMemory();
            if (sampleMemory != null) {
                getSampleCollection(SampleType.MEMORY).add(sampleMemory);
            }
            sampleMemory = sampleCpu();
            if (sampleMemory != null) {
                getSampleCollection(SampleType.CPU).add(sampleMemory);
            }
            samplerLock.unlock();
        } catch (Throwable th) {
            samplerLock.unlock();
        }
    }

    private Sample sampleMemory() {
        return sampler == null ? null : sampleMemory(sampler.activityManager);
    }

    private Sample sampleMemory(ActivityManager activityManager) {
        int totalPss = activityManager.getProcessMemoryInfo(PID)[0].getTotalPss();
        if (totalPss < 0) {
            return null;
        }
        Sample sample = new Sample(SampleType.MEMORY);
        sample.setSampleValue(((double) totalPss) / 1024.0d);
        return sample;
    }

    private Sample sampleCpu() {
        if (cpuSamplingDisabled) {
            return null;
        }
        try {
            if (this.procStatFile == null || this.appStatFile == null) {
                this.procStatFile = new RandomAccessFile("/proc/stat", "r");
                this.appStatFile = new RandomAccessFile("/proc/" + PID[0] + "/stat", "r");
            } else {
                this.procStatFile.seek(0);
                this.appStatFile.seek(0);
            }
            String readLine = this.procStatFile.readLine();
            String readLine2 = this.appStatFile.readLine();
            String[] split = readLine.split(" ");
            String[] split2 = readLine2.split(" ");
            long parseLong = (((((Long.parseLong(split[2]) + Long.parseLong(split[3])) + Long.parseLong(split[4])) + Long.parseLong(split[5])) + Long.parseLong(split[6])) + Long.parseLong(split[7])) + Long.parseLong(split[8]);
            long parseLong2 = Long.parseLong(split2[13]) + Long.parseLong(split2[14]);
            if (this.lastCpuTime == null && this.lastAppCpuTime == null) {
                this.lastCpuTime = Long.valueOf(parseLong);
                this.lastAppCpuTime = Long.valueOf(parseLong2);
                return null;
            }
            Sample sample = new Sample(SampleType.CPU);
            sample.setSampleValue((((double) (parseLong2 - this.lastAppCpuTime.longValue())) / ((double) (parseLong - this.lastCpuTime.longValue()))) * 100.0d);
            this.lastCpuTime = Long.valueOf(parseLong);
            this.lastAppCpuTime = Long.valueOf(parseLong2);
            return sample;
        } catch (Exception e) {
            cpuSamplingDisabled = true;
            log.debug("Exception hit while CPU sampling: " + e.getMessage());
            return null;
        }
    }

    private void resetCpuSampler() {
        this.lastCpuTime = null;
        this.lastAppCpuTime = null;
        if (this.appStatFile != null && this.procStatFile != null) {
            try {
                this.appStatFile.close();
                this.procStatFile.close();
                this.appStatFile = null;
                this.procStatFile = null;
            } catch (IOException e) {
                log.debug("Exception hit while resetting CPU sampler: " + e.getMessage());
            }
        }
    }

    private Map<SampleType, Collection<Sample>> copySamples() {
        samplerLock.lock();
        if (sampler == null) {
            samplerLock.unlock();
            return new HashMap();
        }
        Map enumMap = new EnumMap(sampler.samples);
        for (SampleType sampleType : sampler.samples.keySet()) {
            enumMap.put(sampleType, new ArrayList((Collection) sampler.samples.get(sampleType)));
        }
        samplerLock.unlock();
        return Collections.unmodifiableMap(enumMap);
    }

    private Collection<Sample> getSampleCollection(SampleType sampleType) {
        return (Collection) this.samples.get(sampleType);
    }

    private void start() {
        log.debug("Sampler start");
        samplerLock.lock();
        if (sampler == null) {
            samplerLock.unlock();
            return;
        }
        if (!this.isRunning.get()) {
            clear();
            this.isRunning.set(true);
        }
        samplerLock.unlock();
        sample();
        log.debug("Sampler started");
    }

    private void stop() {
        sample();
        samplerLock.lock();
        if (this.isRunning.get()) {
            this.isRunning.set(false);
            resetCpuSampler();
            samplerLock.unlock();
            log.debug("Sampler stopped");
            return;
        }
        samplerLock.unlock();
    }

    private void clear() {
        for (Collection clear : this.samples.values()) {
            clear.clear();
        }
    }
}
