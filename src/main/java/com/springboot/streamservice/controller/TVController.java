package com.springboot.streamservice.controller;

import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.CommonService;
import com.springboot.streamservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import com.springboot.streamservice.service.TVService;

@RestController
@RequestMapping("/tv")
@CrossOrigin(origins = "*")
public class TVController {

    @Autowired
    TVService tvService;

    @Autowired
    CommonService commonService;

    @GetMapping(value = "/searchTv", produces = "application/json")
    public String searchMovie(@RequestParam(value = "name") String name,
                              @RequestParam(value = "page", required = false) String page) {
        int pageNo = null != page && (Integer.parseInt(page) > 1) ? Integer.parseInt(page) : 1;
        return tvService.searchTVByName(name, pageNo);
    }

    @GetMapping(value = "/getSeason/{id}", produces = "application/json")
    public String getSeasons(@PathVariable String id) {
        return tvService.getTvById(id);
    }

    @GetMapping(value = "/trendingTv", produces = "application/json")
    public String trendingMovies(@RequestParam(value = "page", required = false) String page) {
        int pageNo = null != page && (Integer.parseInt(page) > 1) ? Integer.parseInt(page) : 1;
        return commonService.trendingMovies(pageNo, StreamConstants.TV);
    }
}
