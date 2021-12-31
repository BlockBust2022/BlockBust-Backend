package com.springboot.streamservice.service;

public interface TVService {

    String searchTVByName(String name, int pageNo);

    String getTvById(String id);

    String getEpisodes(String id, String seasonNo);
}
