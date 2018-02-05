package com.mqunar.necro.agent.sample;

public class Sample {
    private SampleValue sampleValue;
    private long timestamp;
    private SampleType type;

    public enum SampleType {
        MEMORY,
        CPU
    }

    public Sample(SampleType sampleType) {
        setSampleType(sampleType);
        setTimestamp(System.currentTimeMillis());
    }

    public Sample(long j) {
        setTimestamp(j);
    }

    public Sample(long j, SampleValue sampleValue) {
        setTimestamp(j);
        setSampleValue(sampleValue);
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    public SampleValue getSampleValue() {
        return this.sampleValue;
    }

    public void setSampleValue(SampleValue sampleValue) {
        this.sampleValue = sampleValue;
    }

    public void setSampleValue(double d) {
        this.sampleValue = new SampleValue(d);
    }

    public void setSampleValue(long j) {
        this.sampleValue = new SampleValue(j);
    }

    public Number getValue() {
        return this.sampleValue.getValue();
    }

    public SampleType getSampleType() {
        return this.type;
    }

    public void setSampleType(SampleType sampleType) {
        this.type = sampleType;
    }

    public String toString() {
        return "type=" + this.type.toString() + ",val=" + this.sampleValue.getValue().toString();
    }
}
