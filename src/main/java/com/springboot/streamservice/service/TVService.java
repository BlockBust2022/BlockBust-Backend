package com.springboot.streamservice.service;

import org.springframework.stereotype.Component;

@Component
public interface TVService {

	String searchTVByName(String name, int pageNo);

}
