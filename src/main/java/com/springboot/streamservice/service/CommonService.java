package com.springboot.streamservice.service;

import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.bean.SearchResponse;
import com.springboot.streamservice.bean.Stream;
import com.springboot.streamservice.bean.UserBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommonService {

    public String featured();

    public ResponseEntity getFeatured();

    public ResponseEntity updateFeatured(Featured featured, String operation);

    public String trendingMovies(int page, String source);

    public String similarMovies(String id, String source);

    public ResponseEntity search(String name, int pageNo);

    public String moveToDb();

    public String registerUser(UserBean user);

    public ResponseEntity getStreamData(int pageNo, int limitNo);

    public ResponseEntity updateStreamData(Stream stream, String operation);
}
