package com.springboot.streamservice.dao.impl;

import com.springboot.streamservice.bean.MovieDbBean;
import com.springboot.streamservice.dao.StreamTapeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StreamTapeDaoImpl implements StreamTapeDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<MovieDbBean> findByImdbId(String imdbId) {
        return jdbcTemplate.query("SELECT * from stream where imdbid = ?", BeanPropertyRowMapper.newInstance(MovieDbBean.class), imdbId);
    }
}
