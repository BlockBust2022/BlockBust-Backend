package com.springboot.streamservice.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.streamservice.bean.tmbdbean.Genre;

@Component
public class MovieResponse{
    public boolean adult;
    public String backdrop_path;
    public List<Genre> genres;
    public int id;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public String poster_path;
    public String release_date;
    public int runtime;
    public String title;
}
