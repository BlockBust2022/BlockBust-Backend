package com.springboot.streamservice.bean;

import com.springboot.streamservice.bean.tmbdbean.Episode;

import java.util.List;

public class TvEpisodeResponse {
    public String _id;
    public String air_date;
    public List<Episode> episodes;
    public String name;
    public String overview;
    public int id;
    public String poster_path;
    public int season_number;
}
