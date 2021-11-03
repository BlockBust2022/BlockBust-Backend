package com.springboot.streamservice.service;

import org.springframework.stereotype.Component;

public interface TVService {

	public String searchTVByName(String name, int pageNo);

	public String getTvById(String name);

//	public String getEpisode(String id, String season);
}
