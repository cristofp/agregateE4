package com.plachno.agregate;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Krzysztof Płachno on 2016-05-01.
 */
public class ArgsValues {
    Double [] args;
    Double [] values;

    public ArgsValues(Double[] args, Double[] values) {
        this.args = args;
        this.values = values;
    }

    public double[] getArgs() {
        return ArrayUtils.toPrimitive(args);
    }

    public void setArgs(Double[] args) {
        this.args = args;
    }

    public double[] getValues() {
        return ArrayUtils.toPrimitive(values);
    }

    public void setValues(Double[] values) {
        this.values = values;
    }
}
