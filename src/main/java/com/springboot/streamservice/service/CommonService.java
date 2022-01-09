package com.springboot.streamservice.service;

import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.bean.UserBean;

import java.util.List;

public interface CommonService {

    public String featured();

    public void updateFeatured(Featured featured);

    public String trendingMovies(int page, String source);

    public String similarMovies(String id, String source);

    public String search(String name, int pageNo);

    public String moveToDb();

    public String registerUser(UserBean user);
}
