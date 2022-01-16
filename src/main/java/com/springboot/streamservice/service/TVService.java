package com.springboot.streamservice.service;

public interface TVService {

    String getTvById(String id);

    String getEpisodes(String id, String seasonNo);
}
