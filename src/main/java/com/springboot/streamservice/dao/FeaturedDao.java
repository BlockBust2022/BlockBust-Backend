package com.springboot.streamservice.dao;

import com.springboot.streamservice.bean.Featured;

import java.util.List;

public interface FeaturedDao {

    public List<Featured> featured();

    public void insertFeatured (Featured featured);
}
