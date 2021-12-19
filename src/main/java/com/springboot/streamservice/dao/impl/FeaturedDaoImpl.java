package com.springboot.streamservice.dao.impl;

import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.bean.MovieDbBean;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.FeaturedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeaturedDaoImpl implements FeaturedDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<Featured> featured() {
        return jdbcTemplate.query("SELECT * from featured", BeanPropertyRowMapper.newInstance(Featured.class));
    }

    @Override
    public void insertFeatured(Featured featured) {
        try{
            if(StreamConstants.INSERT.equalsIgnoreCase(featured.getOperation())){
                jdbcTemplate.update(
                        "INSERT INTO featured (imdbid, type) VALUES (?, ?)",
                        featured.getImdbId(), featured.getType()
                );
            }else if (StreamConstants.DELETE.equalsIgnoreCase(featured.getOperation())){
                jdbcTemplate.update(
                        "DELETE FROM featured WHERE imdbid = ?",
                        featured.getImdbId()
                );
            }

        } catch (Exception e){
            System.err.println(e);
        }

    }
}
