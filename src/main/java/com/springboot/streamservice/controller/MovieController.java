package com.springboot.streamservice.controller;

import com.springboot.streamservice.constants.StreamConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.springboot.streamservice.service.MovieService;

@RestController
@RequestMapping("/movie")
@CrossOrigin(origins = "*")
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

    @GetMapping(value = "/trendingMovies", produces = "application/json")
    public String trendingMovies(@RequestParam(value = "page", required = false) String page) {
        int pageNo = null != page && (Integer.parseInt(page) > 1) ? Integer.parseInt(page) : 1;
        return movieService.trendingMovies(pageNo, StreamConstants.MOVIE);
    }

    @GetMapping(value = "/similarMovies/{id}", produces = "application/json")
    public String similarMovies(@PathVariable String id) {
        return movieService.similarMovies(id);
    }

    @GetMapping(value = "/moveToDb", produces = "application/json")
//    @Scheduled(cron = "0 * * ? * *")
    public String moveToDb() {
        System.out.println("Testing");

        return "here";
    }

}