package com.springboot.streamservice.controller;

import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.CommonService;
import com.springboot.streamservice.service.TVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tv")
@CrossOrigin(origins = "*")
public class TVController {

    @Autowired
    TVService tvService;

    @Autowired
    CommonService commonService;

    @GetMapping(value = "/getSeason/{id}", produces = "application/json")
    public ResponseEntity<?> getSeasons(@PathVariable String id) {
        return tvService.getTvById(id);
    }

    @GetMapping(value = "/getEpisodes/{id}", produces = "application/json")
    public String getEpisodes(@PathVariable String id, @RequestParam(value = "season") String seasonNo) {
        return tvService.getEpisodes(id, seasonNo);
    }

    @GetMapping(value = "/trendingTv", produces = "application/json")
    public String trendingMovies(@RequestParam(value = "page", required = false) String page) {
        int pageNo = null != page && (Integer.parseInt(page) > 1) ? Integer.parseInt(page) : 1;
        return commonService.trendingMovies(pageNo, StreamConstants.TV);
    }

    @GetMapping(value = "/similarTv/{id}", produces = "application/json")
    public String similarMovies(@PathVariable String id) {
        return commonService.similarMovies(id, StreamConstants.TV);
    }
}
