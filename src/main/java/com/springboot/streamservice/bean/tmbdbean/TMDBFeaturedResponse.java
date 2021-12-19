package com.springboot.streamservice.bean.tmbdbean;

import com.springboot.streamservice.bean.MovieResponse;
import com.springboot.streamservice.bean.TvSeasonResponse;

import java.util.List;

public class TMDBFeaturedResponse {
    public List<MovieResponse> movie_results;
    public List<TvSeasonResponse> tv_results;
}
