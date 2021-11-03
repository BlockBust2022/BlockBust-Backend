package com.springboot.streamservice.service;

import org.springframework.stereotype.Component;

@Component
public interface TVService {

	public String searchTVByName(String name, int pageNo);

	public String getMovieById(String name);

	public String getEpisode(String id, String season);
}
