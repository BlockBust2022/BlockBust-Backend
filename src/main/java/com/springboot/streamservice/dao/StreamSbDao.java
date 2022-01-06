package com.springboot.streamservice.dao;

public interface StreamSbDao {
    void moveToDb(String fileCode, String title);
}
