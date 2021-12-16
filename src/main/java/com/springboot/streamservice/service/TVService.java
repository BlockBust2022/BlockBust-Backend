package com.springboot.streamservice.service;

import org.springframework.stereotype.Component;

public interface TVService {

    String searchTVByName(String name, int pageNo);

    String getTvById(String name);
}
