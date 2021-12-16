package com.springboot.streamservice.service;

import org.springframework.stereotype.Component;

@Component
public interface MovieService {

	public String searchMovieByName(String name, int page);
	
	public String getMovieByid(String id);

	public String trendingMovies(int page, String source);

	public String similarMovies(String id);

	public void moveToDb();
}
