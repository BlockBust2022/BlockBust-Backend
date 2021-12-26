package com.springboot.streamservice.service;

import com.springboot.streamservice.bean.Featured;

import java.util.List;

public interface CommonService {

    public String featured();

    public void updateFeatured(Featured featured);

    public String trendingMovies(int page, String source);

    public String similarMovies(String id, String source);
}
