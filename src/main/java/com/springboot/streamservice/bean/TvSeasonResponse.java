package com.springboot.streamservice.bean;

import com.springboot.streamservice.bean.tmbdbean.Genre;
import com.springboot.streamservice.bean.tmbdbean.Seasons;

import java.util.*;

public class TvSeasonResponse {
    public String backdrop_path;
    public String first_air_date;
    public List<Genre> genres;
    public int id;
    public String name;
    public Object next_episode_to_air;
    public int number_of_episodes;
    public int number_of_seasons;
    public List<String> origin_country;
    public String original_language;
    public String original_name;
    public String overview;
    public String poster_path;
    public List<Seasons> seasons;
}
