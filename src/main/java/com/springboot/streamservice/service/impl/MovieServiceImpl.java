package com.springboot.streamservice.service.impl;

import java.util.*;

import com.springboot.streamservice.dao.StreamTapeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.springboot.streamservice.bean.MovieResponse;
import com.springboot.streamservice.bean.MovieSearchResponse;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

    @Value("${streamtape.login}")
    private String login;

    @Value("${streamtape.key}")
    private String key;

    @Value("${tmdb.key}")
    private String tmdbKey;

    @Value("${streamtape.folder}")
    private String folder;

    @Autowired
    StreamTapeDAO streamTapeDAO;

    @Override
    public String getMovieByid(String id) {

        String tmbdUrl = StreamConstants.TMDB_URL + "/movie/" + id + StreamConstants.TMDB_API;

        tmbdUrl = tmbdUrl.replace("{key}", tmdbKey);

        MovieResponse tmbdJson = WebClient.create().get().uri(tmbdUrl).retrieve().bodyToMono(MovieResponse.class)
                .block();

        String imdbId = tmbdJson.imdb_id;

        tmbdJson.setUrl(streamTapeDAO.generateUrl(imdbId));

        return new Gson().toJson(tmbdJson);

    }

    @Override
    public String searchMovieByName(String name, int page) {

        String url = StreamConstants.TMDB_URL + "/search/movie" + StreamConstants.TMDB_API + "&query=" + name + "&page="
                + page;

        url = url.replace("{key}", tmdbKey);

        MovieSearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(MovieSearchResponse.class).block();

        return new Gson().toJson(res);
    }

}