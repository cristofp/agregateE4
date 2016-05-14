package com.plachno.agregate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Krzysztof Płachno on 2016-05-01.
 */
public class Configuration {
    private double frequency;
    private DataSource acc_x;
    private DataSource acc_y;
    private DataSource acc_z;
    private DataSource bvb;
    private DataSource eda;
    private DataSource hr;
    private DataSource ibi;
    private DataSource temp;
    private DataSource tags;
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }


    public Configuration(double frequency, DataSource acc_x, DataSource acc_y, DataSource acc_z, DataSource bvb, DataSource eda, DataSource hr, DataSource ibi, DataSource temp, DataSource tags) {
        this.frequency = frequency;
        this.acc_x = acc_x;
        this.acc_y = acc_y;
        this.acc_z = acc_z;
        this.bvb = bvb;
        this.eda = eda;
        this.hr = hr;
        this.ibi = ibi;
        this.temp = temp;
        this.tags = tags;
    }

    public Configuration() {
    }

    public static Configuration obtainConfiguration(String[] args) throws IOException {
        Configuration configuration = null;
        File confFile = new File("conf.json");

        if (!confFile.exists() || !confFile.isFile()) {
            System.out.println("No 'config.json' found. Using default config.");
            configuration = Configuration.obtainDefaultConfiguration();
        }else{
            configuration = mapper.readValue(confFile, Configuration.class);
        }

        for (String arg : args) {
            if(arg.startsWith("-f=")){
                int frequency = Integer.parseInt(arg.substring(3));
                if(frequency<=0 || frequency>512){
                    System.out.println("ERROR: Wrong frequency value: "+frequency);
                    System.out.println("Exiting program without generating output file.");
                    System.exit(-1);
                }else {
                    System.out.println("Frequency of: "+frequency + " specified");
                    configuration.setFrequency(frequency);
                }
            }else {
                System.out.println("Unknown option: "+ arg);
                if(arg.startsWith("-f") || arg.startsWith("-F")){
                    System.out.println("Didn't you wanted to type: '-f=<value>'");
                }
            }
        }

        return configuration;
    }


    Map<TypeOfSource, DataSource> getNotNullDataSources() {
        //tworzymy mapę z elementami TypZrodla --> nazwa pliku
        HashMap<TypeOfSource, DataSource> dataSources = new LinkedHashMap<TypeOfSource, DataSource>();
        dataSources.put(TypeOfSource.BVP, bvb);
        dataSources.put(TypeOfSource.EDA, eda);
        dataSources.put(TypeOfSource.HR, hr);
        dataSources.put(TypeOfSource.IBI, ibi);
        dataSources.put(TypeOfSource.TEMP, temp);
        dataSources.put(TypeOfSource.ACC_X, acc_x);
        dataSources.put(TypeOfSource.ACC_Y, acc_y);
        dataSources.put(TypeOfSource.ACC_Z, acc_z);
//        dataSources.put(TypeOfSource.TAGS, tags);

        //usuwamy z tej mapy wpisy dla których plik nie istnieje (w ogóle nie było w jsonie nazwy pliku, lub nie ma pliku o takiej nazwie
        HashMap<TypeOfSource, DataSource> mapOnlyWithValidEntries = new LinkedHashMap<TypeOfSource, DataSource>(dataSources);
        for (Map.Entry<TypeOfSource, DataSource> entry : dataSources.entrySet()) {
            if (entry.getValue() == null || !(new File(entry.getValue().getFile()).exists())) {
                mapOnlyWithValidEntries.remove(entry.getKey());
            }
        }
        return mapOnlyWithValidEntries;
    }


    public double getFrequency() {
        return frequency;
    }

    public DataSource getAcc_x() {
        return acc_x;
    }

    public DataSource getAcc_y() {
        return acc_y;
    }

    public DataSource getAcc_z() {
        return acc_z;
    }

    public DataSource getBvb() {
        return bvb;
    }

    public DataSource getEda() {
        return eda;
    }

    public DataSource getHr() {
        return hr;
    }

    public DataSource getIbi() {
        return ibi;
    }

    public DataSource getTemp() {
        return temp;
    }

    public DataSource getTags() {
        return tags;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setAcc_x(DataSource acc_x) {
        this.acc_x = acc_x;
    }

    public void setAcc_y(DataSource acc_y) {
        this.acc_y = acc_y;
    }

    public void setAcc_z(DataSource acc_z) {
        this.acc_z = acc_z;
    }

    public void setBvb(DataSource bvb) {
        this.bvb = bvb;
    }

    public void setEda(DataSource eda) {
        this.eda = eda;
    }

    public void setHr(DataSource hr) {
        this.hr = hr;
    }

    public void setIbi(DataSource ibi) {
        this.ibi = ibi;
    }

    public void setTags(DataSource tags) {
        this.tags = tags;
    }

    public void setTemp(DataSource temp) {
        this.temp = temp;
    }

    public static Configuration obtainDefaultConfiguration() throws IOException {
        URL confFileUrl = Configuration.class.getResource("/conf.json");
        return mapper.readValue(confFileUrl, Configuration.class);
    }
}
