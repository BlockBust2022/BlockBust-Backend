package com.springboot.streamservice.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

@Component
public class MovieServiceImpl implements MovieService {

    @Value("${streamtape.login}")
    private String login;

    @Value("${streamtape.key}")
    private String key;

    @Value("${tmdb.key}")
    private String tmdbKey;

    @Value("${streamtape.folder}")
    private String folder;

    @Override
    public String getMovieByid(String id) {

        String tmbdUrl = StreamConstants.TMDB_URL + "/movie/" + id + StreamConstants.TMDB_API;

        tmbdUrl = tmbdUrl.replace("{key}", tmdbKey);

        MovieResponse tmbdJson = WebClient.create().get().uri(tmbdUrl).retrieve().bodyToMono(MovieResponse.class)
                .block();

        String imdbId = tmbdJson.imdb_id;

        String url = StreamConstants.STREAMTAPE_URL + StreamConstants.LIST_FILES + StreamConstants.LOGIN;

        url = url.replace("{login}", login).replace("{key}", key).replace("{folder}", folder);

        String json = WebClient.create().get().uri(url).retrieve().bodyToMono(String.class).block();

        JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);

        String allList = convertedObject.get("result").getAsJsonObject().get("files").toString();

        ObjectMapper mapper = new ObjectMapper();

        List<HashMap<String, String>> myObjects = null;
        try {
            myObjects = mapper.readValue(allList, new TypeReference<List<HashMap<String, String>>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Map<String, String> res = new HashMap<String, String>();
        for (HashMap<String, String> map : myObjects) {

            if (map.get("name").contains(imdbId)) {
                map.put("title", tmbdJson.title);
                res.putAll(map);
            }
        }

        if (res.isEmpty()) {
            String vidCloudUrl = StreamConstants.VIDCLOUD_URL.replace("{imdb}", imdbId);
            res.put("link", vidCloudUrl);
            res.put("title", tmbdJson.title);

        }

        return new Gson().toJson(res);

    }

    @Override
    public String searchMovieByName(String name, int page) {

        String url = StreamConstants.TMDB_URL + "/search/movie" + StreamConstants.TMDB_API + "&query=" + name + "&page="
                + page;

        url = url.replace("{key}", tmdbKey);

        MovieSearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(MovieSearchResponse.class).block();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

}