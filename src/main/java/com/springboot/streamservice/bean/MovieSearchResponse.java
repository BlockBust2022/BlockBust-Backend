package com.springboot.streamservice.bean;

import java.util.List;

import com.springboot.streamservice.bean.tmbdbean.Result;

public class MovieSearchResponse {
	public int page;
	public List<Result> results;
	public int total_pages;
	public int total_results;
}
