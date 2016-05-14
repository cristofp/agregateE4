package com.plachno.agregate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Krzysztof Płachno on 2016-05-01.
 */
public class File2ArgsValues {
    public static ArgsValues convert(File file) throws IOException {
        ArrayList<Double> args = new ArrayList<Double>();
        ArrayList<Double> values = new ArrayList<Double>();
        LineIterator lineIterator = FileUtils.lineIterator(file);

        while (lineIterator.hasNext()){
            String[] split = lineIterator.nextLine().split(",");
            args.add(Double.parseDouble(split[0]));
            values.add(Double.parseDouble(split[1]));
        }

        return new ArgsValues(args.toArray(new Double[args.size()]), values.toArray(new Double[values.size()]));
    }
}
