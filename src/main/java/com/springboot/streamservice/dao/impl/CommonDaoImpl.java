package com.springboot.streamservice.dao.impl;

import com.springboot.streamservice.bean.Featured;
import com.springboot.streamservice.bean.MovieDbBean;
import com.springboot.streamservice.bean.Stream;
import com.springboot.streamservice.bean.UserBean;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommonDaoImpl implements CommonDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<MovieDbBean> findByImdbId(String imdbId) {
        return jdbcTemplate.query("SELECT * from stream where imdbid = ?", BeanPropertyRowMapper.newInstance(MovieDbBean.class), imdbId);
    }

    @Override
    public void moveToDb(String fileCode, String title) {

        jdbcTemplate.update("INSERT INTO stream (url, movie_name)\n" +
                "SELECT * FROM (SELECT ?, ?) AS tmp\n" +
                "WHERE NOT EXISTS (\n" +
                "    SELECT url FROM stream WHERE url = ? \n" +
                ") LIMIT 1;", fileCode, title, fileCode);
    }

    @Override
    public List<Featured> featured() {
        return jdbcTemplate.query("SELECT * from featured", BeanPropertyRowMapper.newInstance(Featured.class));
    }

    @Override
    public String insertFeatured(Featured featured, String operation) {
        try{
            if (StreamConstants.INSERT.equalsIgnoreCase(operation)) {
                jdbcTemplate.update(
                        "INSERT INTO featured (imdbid, type) VALUES (?, ?)",
                        featured.getImdbId(), featured.getType()
                );
            } else if (StreamConstants.DELETE.equalsIgnoreCase(operation)) {
                jdbcTemplate.update(
                        "DELETE FROM featured WHERE id = ?",
                        featured.getId()
                );
            } else {
                return "No Record Updated";
            }

        } catch (Exception e){
            System.err.println(e);
            return "Exception Updating Records";
        }

        return "Success";

    }

    @Override
    public UserBean getUserfromUserName(String userName) {
        List<UserBean> users = jdbcTemplate.query("SELECT * from user where userName = ?", BeanPropertyRowMapper.newInstance(UserBean.class), userName);
        if(users.size() == 1){
            return users.get(0);
        }
        return null;
//        return jdbcTemplate.query("SELECT * from user where userName = ?", userName);
    }

    @Override
    public String insertUser(UserBean user) {
        int i = jdbcTemplate.update("INSERT INTO user (username, password, role) VALUES (?, ?, ?);",
                user.getUserName(), user.getPassword(), "Admin");

        if(i > 0){
            return "User Added Successfully.";
        }
        return "Error Adding User";
    }

    @Override
    public Integer checkIfUserExist(String userName) {
        return jdbcTemplate.queryForObject("select count(*) from user where username = ?", Integer.class, userName);
    }

    @Override
    public List<Stream> getStreamData(int pageNo, int limitNo) {
        int offset = (pageNo - 1) * limitNo;
        return jdbcTemplate.query("select * from stream order by imdbid limit ?,?", BeanPropertyRowMapper.newInstance(Stream.class), offset, limitNo);
    }

    @Override
    public Integer getStreamDataCount() {
        return jdbcTemplate.queryForObject("select count(*) from stream", Integer.class);
    }

    @Override
    public String insertStream(Stream stream, String operation) {
        try {
            if (StreamConstants.INSERT.equalsIgnoreCase(operation)) {
                jdbcTemplate.update(
                        "INSERT INTO stream (url, imdbid, movie_name) VALUES (?, ?, ?)",
                        stream.getUrl(), stream.getImdbid(), stream.getMovie_name()
                );
            } else if (StreamConstants.DELETE.equalsIgnoreCase(operation)) {
                jdbcTemplate.update(
                        "DELETE FROM stream WHERE id = ?",
                        stream.getId()
                );
            } else if (StreamConstants.UPDATE.equalsIgnoreCase(operation) && 0 != stream.getId()) {
                jdbcTemplate.update(
                        "UPDATE stream set imdbid = ?, url = ?, movie_name = ? where id = ?",
                        stream.getImdbid(), stream.getUrl(), stream.getMovie_name(), stream.getId()
                );
            } else {
                return "No Record Updated";
            }

        } catch (Exception e) {
            System.err.println(e);
            return "Exception Updating Records";
        }

        return "Success";

    }
}
