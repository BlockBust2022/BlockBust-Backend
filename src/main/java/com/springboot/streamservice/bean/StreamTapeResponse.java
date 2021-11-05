package com.springboot.streamservice.bean;

import com.springboot.streamservice.bean.tmbdbean.StreamTapeResult;

import java.util.List;

public class StreamTapeResponse {
    public int status;
    public String msg;
    public StreamTapeResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public StreamTapeResult getResult() {
        return result;
    }

    public void setResult(StreamTapeResult result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
