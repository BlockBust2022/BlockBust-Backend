package com.springboot.streamservice.dao;

import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.bean.MovieDbBean;
import com.springboot.streamservice.bean.Stream;
import com.springboot.streamservice.bean.UserBean;

import java.util.List;

public interface CommonDao {

    public List<MovieDbBean> findByImdbId(String imdbId);

    public void moveToDb(String fileCode, String title);

    public List<Featured> featured();

    public String insertFeatured (Featured featured, String operation);

    public UserBean getUserfromUserName (String userName);

    public String insertUser (UserBean user);

    public Integer checkIfUserExist (String userName);

    public List<Stream> getStreamData(int pageNo, int limitNo);

    public Integer getStreamDataCount();

    public String insertStream (Stream stream, String operation);
}
