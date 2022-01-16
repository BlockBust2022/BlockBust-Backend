package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.springboot.streamservice.bean.*;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.CommonDao;
import com.springboot.streamservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

        tmbdJson.setUrl(generateUrl(imdbId));

        return new Gson().toJson(tmbdJson);

    }

    public String generateUrl(String imdbId) {

        try {
            List<MovieDbBean> movieDbBeans = commonDao.findByImdbId(imdbId);

            for (MovieDbBean file : movieDbBeans) {
                if (file.getImdbid().equalsIgnoreCase(imdbId)) {
                    return StreamConstants.STREAMSB_WATCH_URL + file.getUrl();
                }
            }
        } catch (Exception e) {

        }

        return StreamConstants.SERVER_URL.replace("{imdb}", imdbId);
    }

}