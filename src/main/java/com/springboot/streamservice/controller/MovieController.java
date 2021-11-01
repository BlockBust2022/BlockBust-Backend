package com.springboot.streamservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.streamservice.service.MovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	MovieService movieService;

	@GetMapping(value = "/searchMovie", produces = "application/json")
	public String searchMovie(@RequestParam(value = "name") String name,
			@RequestParam(value = "page", required = false) String page) {
		int pageNo = null != page && (Integer.parseInt(page) > 1) ? Integer.parseInt(page) : 1;
		return movieService.searchMovieByName(name, pageNo);
	}

	@GetMapping(value = "/getMovie/{id}", produces = "application/json")
	public String findMovie(@PathVariable String id) {
		return movieService.getMovieByid(id);
	}

}