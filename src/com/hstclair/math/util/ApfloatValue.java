package com.hstclair.math.util;

import com.hstclair.math.matrix.Value;
import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;

/**
 * Created by hstclair on 4/22/17.
 */
public class ApfloatValue implements Value<Apfloat> {

    final Apfloat value;

    public ApfloatValue(Apfloat value) {
        this.value = value;
    }

    @Override
    public Value<Apfloat> multiply(Value<Apfloat> multiplicand) {
        return new ApfloatValue(this.value.multiply(multiplicand.value()));
    }

    @Override
    public Apfloat value() {
        return value;
    }

    @Override
    public Value<Apfloat> divide(Value<Apfloat> divisor) {
        return new ApfloatValue(this.value.divide(divisor.value()));
    }

    @Override
    public Value<Apfloat> add(Value<Apfloat> addend) {
        return new ApfloatValue(this.value.add(addend.value()));
    }

    @Override
    public Value<Apfloat> subtract(Value<Apfloat> minuend) {
        return new ApfloatValue(this.value.subtract(minuend.value()));
    }

    @Override
    public Value<Apfloat> negate() {
        return new ApfloatValue(this.value.negate());
    }

    @Override
    public boolean isZero() {
        return value.equals(Apfloat.ZERO);
    }

    @Override
    public boolean isOne() {
        return value.equals(Apfloat.ONE);
    }
}
