package com.plachno.agregate;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Krzysztof Płachno on 2016-05-01.
 */
public class OutputFileGenerator {

    private final double start;
    private final double stop;
    private final double step;
    private final double frequency;
    private final Map<TypeOfSource, PolynomialSplineFunction> functionsMap;
    private LinkedList<Pair<Double, String>> tagsList;

    public OutputFileGenerator(double frequency, Map<TypeOfSource, PolynomialSplineFunction> functionsMap, LinkedList<Pair<Double, String>> tagsList) throws Exception {
        this.frequency = frequency;
        this.functionsMap = functionsMap;
        this.tagsList = tagsList;
        start = obtainStartTimestamp(functionsMap);
        stop = obtainStopTimestamp(functionsMap);
        step = 1./frequency;
    }
    public void generateFile() throws IOException {
        File output = new File("e4AgregatedData.csv");
        PrintWriter pw = new PrintWriter(new FileWriter(output));

        printHeader(pw, functionsMap.keySet());

        for(double currentTimeStamp = start; currentTimeStamp<stop; currentTimeStamp+=step){
            pw.printf(Locale.US, "%f", currentTimeStamp);
            for (PolynomialSplineFunction function : functionsMap.values()) {
                pw.print("," + function.value(currentTimeStamp));
            }
            pw.print(",");
            printTagIfPresent(pw,currentTimeStamp, step, tagsList);
            pw.println();
        }
        pw.close();
    }

    private void printTagIfPresent(PrintWriter pw, double currentTimeStamp, double step, LinkedList<Pair<Double, String>> tagsList) {
        if(tagsList == null || tagsList.isEmpty()){
            return;
        }
        if(tagsList.getFirst().getKey() - currentTimeStamp < step){
            pw.print(tagsList.getFirst().getValue()+ " ");
            tagsList.removeFirst();
            printTagIfPresent(pw, currentTimeStamp, step, tagsList);
        }
        return;
    }

    private void printHeader(PrintWriter pw, Set<TypeOfSource> typesOfFiles) {
        pw.print("timestamp");
        for (TypeOfSource typesOfFile : typesOfFiles) {
            pw.print("," + typesOfFile.getName());
        }
        pw.println();
    }

    private double obtainStopTimestamp(Map<TypeOfSource, PolynomialSplineFunction> functionsMap) {
        int knotNumber = Integer.MAX_VALUE;
        double stop = 0;
        for (Map.Entry<TypeOfSource, PolynomialSplineFunction> entry : functionsMap.entrySet()) {
            if(knotNumber > entry.getValue().getN()){
                knotNumber = entry.getValue().getN();
                stop = entry.getValue().getKnots()[knotNumber-1];
            }
        }
        return stop;
    }

    private double obtainStartTimestamp(Map<TypeOfSource, PolynomialSplineFunction> functionsMap) throws Exception {
        double start = Double.MIN_VALUE;
        for (Map.Entry<TypeOfSource, PolynomialSplineFunction> entry : functionsMap.entrySet()) {
            if(start < entry.getValue().getKnots()[0]){
                start = entry.getValue().getKnots()[0];
            }
        }
        return start;
    }
}
