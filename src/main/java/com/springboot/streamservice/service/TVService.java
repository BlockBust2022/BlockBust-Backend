package com.springboot.streamservice.service;

import org.springframework.http.ResponseEntity;

public interface TVService {

    ResponseEntity<?> getTvById(String id);

    String getEpisodes(String id, String seasonNo);
}
