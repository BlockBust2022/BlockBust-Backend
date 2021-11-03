package com.springboot.streamservice.controller;

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

//    @GetMapping(value = "/getEpisode/{id}", produces = "application/json")
//    public String getEpisode(@PathVariable String id,
//                            @RequestParam(value = "season") String season) {
//        return tvService.getEpisode(id,season);
//    }
//
//    @GetMapping(value = "/watchEpisode/{id}", produces = "application/json")
//    public String watchEpisode(@PathVariable String id,
//                            @RequestParam(value = "season") String season,
//                            @RequestParam(value = "episode") String episode) {
//        return "id : " + id + " season : " + season + " episode : " + episode;
//    }

}
