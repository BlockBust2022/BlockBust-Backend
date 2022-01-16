package com.springboot.streamservice.service;

import com.springboot.streamservice.bean.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public interface MovieService {

	public String getMovieByid(String id);
}
