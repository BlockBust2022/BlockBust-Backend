package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.springboot.streamservice.bean.*;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.CommonDao;
import com.springboot.streamservice.service.MovieService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Value("${tmdb.key}")
    private String tmdbKey;

    @Autowired
    private CommonDao commonDao;

    @Override
    public String getMovieByid(String id) {

        String tmbdUrl = StreamConstants.TMDB_URL + "/movie/" + id + StreamConstants.TMDB_API;

        tmbdUrl = tmbdUrl.replace("{key}", tmdbKey);

        MovieResponse tmbdJson = WebClient.create().get().uri(tmbdUrl).retrieve().bodyToMono(MovieResponse.class)
                .block();

        String imdbId = tmbdJson.imdb_id;

        if(imdbId == null) {
//        	String jsonString = new JSONObject().put("error", "Not Found").toString();
        	return new Gson().toJson("Not Found");
        }
        
        tmbdJson.setUrl(generateUrl(imdbId));
        
        return new Gson().toJson(tmbdJson);

    }

    public List<String> generateUrl(String imdbId) {

        List<String> server = new ArrayList<>();

        try {
            List<MovieDbBean> movieDbBeans = commonDao.findByImdbId(imdbId);

            for (MovieDbBean file : movieDbBeans) {
                if (file.getImdbid().equalsIgnoreCase(imdbId)) {
                    server.add(StreamConstants.STREAMSB_WATCH_URL + file.getUrl());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        server.add(StreamConstants.SERVER_URL.replace("{imdb}", imdbId));

        return server;
    }

}