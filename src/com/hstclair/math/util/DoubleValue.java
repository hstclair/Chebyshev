package com.hstclair.math.util;

import com.hstclair.math.matrix.Value;

/**
 * Created by hstclair on 4/22/17.
 */
public class DoubleValue implements Value<Double> {

    final Double value;

    public DoubleValue(Double value) {
        this.value = value;
    }

    @Override
    public Value<Double> multiply(Value<Double> multiplicand) {
        return new DoubleValue(value * multiplicand.value());
    }

    @Override
    public Value<Double> divide(Value<Double> divisor) {
        return new DoubleValue(value / divisor.value());
    }

    @Override
    public Value<Double> add(Value<Double> addend) {
        return new DoubleValue(value + addend.value());
    }

    @Override
    public Value<Double> subtract(Value<Double> minuend) {
        return new DoubleValue(value - minuend.value());
    }

    @Override
    public Value<Double> negate() {
        return new DoubleValue(-value);
    }

    @Override
    public Double value() {
        return value;
    }
}
