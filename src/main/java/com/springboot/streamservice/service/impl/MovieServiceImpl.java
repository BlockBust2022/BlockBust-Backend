package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.springboot.streamservice.bean.MovieResponse;
import com.springboot.streamservice.bean.MovieSearchResponse;
import com.springboot.streamservice.bean.StreamTapeResponse;
import com.springboot.streamservice.bean.tmbdbean.StreamTapeFile;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Override
    public String getMovieByid(String id) {

        String tmbdUrl = StreamConstants.TMDB_URL + "/movie/" + id + StreamConstants.TMDB_API;

        tmbdUrl = tmbdUrl.replace("{key}", tmdbKey);

        MovieResponse tmbdJson = WebClient.create().get().uri(tmbdUrl).retrieve().bodyToMono(MovieResponse.class)
                .block();

        String imdbId = tmbdJson.imdb_id;

        tmbdJson.setUrl(generateUrl(imdbId));

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

    public String generateUrl(String imdbId) {
        String url = StreamConstants.STREAMTAPE_URL + StreamConstants.LIST_FILES + StreamConstants.LOGIN;

        url = url.replace("{login}", login).replace("{key}", key).replace("{folder}", folder);

        StreamTapeResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(StreamTapeResponse.class).block();

        if(200 == res.getStatus()) {
            for (StreamTapeFile file : res.getResult().getFiles()) {
                if (file.getName().contains(imdbId)) {
                    return file.getLink();
                }
            }

        }

        return StreamConstants.VIDCLOUD_URL.replace("{imdb}", imdbId);
    }

}