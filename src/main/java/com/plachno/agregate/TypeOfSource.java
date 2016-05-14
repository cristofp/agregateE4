package com.plachno.agregate;

/**
 * Created by Krzysztof Płachno on 2016-05-01.
 */
public enum TypeOfSource {
    ACC_X("ACC_X"), ACC_Y("ACC_Y"), ACC_Z("ACC_Z"), BVP("BVB"), EDA("EDA"), HR("HR"), IBI("IBI"), TEMP("TEMP"), TAGS("TAGS");

    private String name;

    TypeOfSource(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public static TypeOfSource obtainType(String name){
        for (TypeOfSource typeOfFile : values()) {
            if(typeOfFile.getName().equals(name)){
                return typeOfFile;
            }
        }
        return null;
    }
}
