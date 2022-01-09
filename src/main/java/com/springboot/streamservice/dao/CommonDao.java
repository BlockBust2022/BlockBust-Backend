package com.springboot.streamservice.dao;

import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.bean.MovieDbBean;
import com.springboot.streamservice.bean.UserBean;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Map;

public interface CommonDao {

    public List<MovieDbBean> findByImdbId(String imdbId);

    public void moveToDb(String fileCode, String title);

    public List<Featured> featured();

    public void insertFeatured (Featured featured);

    public UserBean getUserfromUserName (String userName);

    public String insertUser (UserBean user);

    public Integer checkIfUserExist (String userName);
}
