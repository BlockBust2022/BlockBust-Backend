package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.springboot.streamservice.bean.*;
import com.springboot.streamservice.bean.tmbdbean.Result;
import com.springboot.streamservice.bean.tmbdbean.StreamTapeFile;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.StreamTapeDao;
import com.springboot.streamservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

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
    StreamTapeDao streamTapeDao;

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
    public void moveToDb() {

        String url = StreamConstants.STREAMTAPE_URL + StreamConstants.LIST_FILES + StreamConstants.LOGIN;

        url = url.replace("{login}", login).replace("{key}", key).replace("{folder}", folder);

        StreamTapeResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(StreamTapeResponse.class).block();

        if (200 == res.getStatus()) {
            for (StreamTapeFile file : res.getResult().getFiles()) {
//                if (file.getName().contains(imdbId)) {
//                    return StreamConstants.STREAMTAPE_MOVIE_URL + file.getLinkid();
//                }
            }

        }


//        INSERT INTO streamdb.stream (url, imdbid)
//        SELECT * FROM (SELECT 'asda', 'asdasd') AS tmp
//        WHERE NOT EXISTS (
//                SELECT url FROM streamdb.stream WHERE url = 'asda'
//        ) LIMIT 1;
    }

    @Override
    public String searchMovieByName(String name, int page) {

        String url = StreamConstants.TMDB_URL + "/search/movie" + StreamConstants.TMDB_API + "&query=" + name + "&page="
                + page;

        url = url.replace("{key}", tmdbKey);

        SearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(SearchResponse.class).block();

        return new Gson().toJson(res);
    }

    public String generateUrl(String imdbId) {

        try {
            List<MovieDbBean> movieDbBeans = streamTapeDao.findByImdbId(imdbId);

            for (MovieDbBean file : movieDbBeans) {
                if (file.getImdbid().equalsIgnoreCase(imdbId)) {
                    return StreamConstants.STREAMTAPE_MOVIE_URL + file.getUrl();
                }
            }
        } catch (Exception e) {

        }

        return StreamConstants.SERVER_URL.replace("{imdb}", imdbId);
    }

}