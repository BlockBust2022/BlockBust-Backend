package com.springboot.streamservice.bean;

import java.util.List;

public class TvTrendingResponse {
    public int page;
    public List<TvSeasonResponse> results;
    public int total_pages;
    public int total_results;
}
