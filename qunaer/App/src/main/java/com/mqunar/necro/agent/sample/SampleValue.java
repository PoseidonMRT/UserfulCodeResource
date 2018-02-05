package com.mqunar.necro.agent.sample;

public class SampleValue {
    private boolean isDouble;
    private Double value = Double.valueOf(0.0d);

    public SampleValue(double d) {
        setValue(d);
    }

    public SampleValue(long j) {
        setValue(j);
    }

    public Number getValue() {
        return Double.valueOf(this.isDouble ? asDouble().doubleValue() : (double) asLong().longValue());
    }

    public Double asDouble() {
        return this.value;
    }

    public Long asLong() {
        return Long.valueOf(this.value.longValue());
    }

    public void setValue(double d) {
        this.value = Double.valueOf(d);
        this.isDouble = true;
    }

    public void setValue(long j) {
        this.value = Double.valueOf((double) j);
        this.isDouble = false;
    }

    public boolean isDouble() {
        return this.isDouble;
    }

    public void setDouble(boolean z) {
        this.isDouble = z;
    }
}
