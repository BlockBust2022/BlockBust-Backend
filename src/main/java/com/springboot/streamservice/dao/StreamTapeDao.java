package com.springboot.streamservice.dao;

import com.springboot.streamservice.bean.MovieDbBean;

import java.util.List;

public interface StreamTapeDao {
    public List<MovieDbBean> findByImdbId(String imdbId);
}
