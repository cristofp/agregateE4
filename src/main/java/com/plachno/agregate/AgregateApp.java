package com.plachno.agregate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Hello world!
 */
public class AgregateApp {

    private static volatile SplineInterpolator splineInterpolator = new SplineInterpolator();

    public static void main(String[] args) throws Exception {
        Configuration configuration = Configuration.obtainConfiguration(args);
        assert configuration != null;

        Map<TypeOfSource, ArgsValues> argsValuesMap = obtainArgsValuesMap(configuration);
        Map<TypeOfSource, PolynomialSplineFunction> functionsMap = obtainFunctions(argsValuesMap);
        LinkedList<Pair<Double, String>> tagsMap = obtainTags(configuration);

        OutputFileGenerator outputFileGenerator = new OutputFileGenerator(configuration.getFrequency(), functionsMap, tagsMap);
        outputFileGenerator.generateFile();

    }

    private static LinkedList<Pair<Double, String>> obtainTags(Configuration configuration) throws IOException {
        if(configuration.getTags()== null || !configuration.getTags().toFile().isFile()){
            return null;
        }
        LinkedList<Pair<Double, String>> tagsList = new LinkedList<Pair<Double, String>>();
        LineIterator lineIterator = FileUtils.lineIterator(configuration.getTags().toFile());

        while (lineIterator.hasNext()){
            String[] split = lineIterator.nextLine().split(",");
            tagsList.addLast( new Pair<Double, String>(Double.parseDouble(split[0]), split[1]));
        }
        return tagsList;
    }

    private static Map<TypeOfSource,PolynomialSplineFunction> obtainFunctions(Map<TypeOfSource, ArgsValues> argsValues) {
        Map<TypeOfSource,PolynomialSplineFunction> functionsMap = new LinkedHashMap<TypeOfSource, PolynomialSplineFunction>();

        for (Map.Entry<TypeOfSource, ArgsValues> entry: argsValues.entrySet()) {
            functionsMap.put(entry.getKey(), splineInterpolator.interpolate(entry.getValue().getArgs(), entry.getValue().getValues()));
        }
        return functionsMap;
    }

    private static Map<TypeOfSource, ArgsValues> obtainArgsValuesMap(Configuration configuration) throws IOException {
        Map<TypeOfSource, DataSource> notNullDataSources = configuration.getNotNullDataSources();
        //pętla
        Map<TypeOfSource, ArgsValues> argsValuesMap = new LinkedHashMap<TypeOfSource, ArgsValues>();
        if(notNullDataSources.isEmpty()){
            System.out.println("No input _TS.csv files found.");
            System.exit(-1);
        }
        for (Map.Entry<TypeOfSource, DataSource> entry : notNullDataSources.entrySet()) {
            ArgsValues argsValues = File2ArgsValues.convert(entry.getValue().toFile());
            argsValuesMap.put(entry.getKey(), argsValues);
        }
        return argsValuesMap;
    }
}
