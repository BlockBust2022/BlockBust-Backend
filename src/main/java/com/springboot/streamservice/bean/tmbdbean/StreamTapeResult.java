package com.springboot.streamservice.bean.tmbdbean;

import java.util.List;

public class StreamTapeResult {
    public List<StreamTapeFile> files;

    public List<StreamTapeFile> getFiles() {
        return files;
    }

    public void setFiles(List<StreamTapeFile> files) {
        this.files = files;
    }
}
