package com.plachno.agregate;

import java.io.File;

/**
 * Created by Krzysztof Płachno on 2016-05-01.
 */
public class DataSource {
    private String file;

    public DataSource(String file) {
        this.file = file;
    }

    public File toFile() {
        return new File(file);
    }

    public DataSource() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
