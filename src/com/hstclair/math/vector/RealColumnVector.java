package com.hstclair.math.vector;

/**
 * Created by hstclair on 4/17/17.
 */
public class RealColumnVector {

    private RealVector realVector;

    public RealColumnVector(RealVector realVector) {
        this.realVector = realVector;
    }

    public int count() {
        return realVector.count();
    }

    public double member(int index) {
        return realVector.member(index);
    }
}
