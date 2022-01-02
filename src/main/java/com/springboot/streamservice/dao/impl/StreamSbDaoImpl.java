package com.springboot.streamservice.dao.impl;

import com.springboot.streamservice.bean.MovieDbBean;
import com.springboot.streamservice.dao.StreamSbDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StreamSbDaoImpl implements StreamSbDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void moveToDb(String fileCode, String title) {
//        return jdbcTemplate.query("INSERT INTO stream (url, movie_name)\n" +
//                "SELECT * FROM (SELECT '2OolR9q6w3cZgOB', 'asdasd') AS tmp\n" +
//                "WHERE NOT EXISTS (\n" +
//                "    SELECT url FROM stream WHERE url = '2OolR9q6w3cZgOB'\n" +
//                ") LIMIT 1;", BeanPropertyRowMapper.newInstance(MovieDbBean.class), imdbId);

        jdbcTemplate.update("INSERT INTO stream (url, movie_name)\n" +
                        "SELECT * FROM (SELECT ?, ?) AS tmp\n" +
                        "WHERE NOT EXISTS (\n" +
                        "    SELECT url FROM stream WHERE url = ? \n" +
                        ") LIMIT 1;", fileCode, title, fileCode);
    }
}