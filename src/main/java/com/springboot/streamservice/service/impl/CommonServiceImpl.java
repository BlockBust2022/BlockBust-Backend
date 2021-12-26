package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.springboot.streamservice.bean.*;
import com.springboot.streamservice.bean.tmbdbean.Result;
import com.springboot.streamservice.bean.tmbdbean.TMDBFeaturedResponse;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.FeaturedDao;
import com.springboot.streamservice.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Value("${tmdb.key}")
    private String tmdbKey;

    @Autowired
    FeaturedDao featuredDao;

    @Override
    public String featured() {

        List<Featured> featuredList = featuredDao.featured();
        List<FeaturedResponse> featuredResponses = new ArrayList<>();

        for (Featured featured : featuredList) {
            String tmbdUrl = StreamConstants.TMDB_URL + "/find/" + featured.getImdbId() + StreamConstants.TMDB_API + "&external_source=imdb_id";
            tmbdUrl = tmbdUrl.replace("{key}", tmdbKey);

            TMDBFeaturedResponse res = WebClient.create().get().uri(tmbdUrl).retrieve().bodyToMono(TMDBFeaturedResponse.class)
                    .block();

            FeaturedResponse featuredRes = new FeaturedResponse();

            if (null != res.movie_results && !res.movie_results.isEmpty()) {

                featuredRes.setRelease_date(res.movie_results.get(0).getRelease_date());
                featuredRes.setBackdrop_path(res.movie_results.get(0).getBackdrop_path());
                featuredRes.setId(res.movie_results.get(0).getId());
                featuredRes.setPoster_path(res.movie_results.get(0).getPoster_path());
                featuredRes.setSource(StreamConstants.MOVIE);
                featuredRes.setTitle(res.movie_results.get(0).getTitle());

            } else if (null != res.tv_results && !res.tv_results.isEmpty()) {

                featuredRes.setRelease_date(res.tv_results.get(0).getFirst_air_date());
                featuredRes.setBackdrop_path(res.tv_results.get(0).getBackdrop_path());
                featuredRes.setId(res.tv_results.get(0).getId());
                featuredRes.setPoster_path(res.tv_results.get(0).getPoster_path());
                featuredRes.setSource(StreamConstants.TV);
                featuredRes.setTitle(res.tv_results.get(0).getName());

            }

            featuredResponses.add(featuredRes);

        }

        return new Gson().toJson(featuredResponses);
    }

    @Override
    public void updateFeatured(Featured featured) {
//        featuredDao.insertFeatured(featured);
    }

    @Override
    public String trendingMovies(int page, String source) {

        String url = StreamConstants.TMDB_URL + "trending/" + source + "/day" + StreamConstants.TMDB_API + "&page="
                + page;

        url = url.replace("{key}", tmdbKey);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date today = new Date();

        try {

            if (StreamConstants.MOVIE.equalsIgnoreCase(source)) {
                SearchResponse res;
                Iterator<Result> iter;

                res = WebClient.create().get().uri(url).retrieve().bodyToMono(SearchResponse.class).block();
                iter = res.results.iterator();

                while (iter.hasNext()) {
                    Date newDate = sdf.parse(iter.next().getRelease_date());
                    if (newDate.after(today)) {
                        iter.remove();
                    }
                }

                return new Gson().toJson(res);

            } else if (StreamConstants.TV.equalsIgnoreCase(source)) {
                TvTrendingResponse tvRes;
                Iterator<TvSeasonResponse> tvIter;

                tvRes = WebClient.create().get().uri(url).retrieve().bodyToMono(TvTrendingResponse.class).block();
                tvIter = tvRes.results.iterator();

                while (tvIter.hasNext()) {
                    Date newDate = sdf.parse(tvIter.next().getFirst_air_date());

                    if (newDate.after(today)) {
                        tvIter.remove();
                    }
                }

                return new Gson().toJson(tvRes);

            }
        } catch (Exception e) {
            System.err.println("Movie Service Impl || trendingMovies ||" + e);
        }

        return new Gson().toJson("{\"Error\": \"invalid\"}");
    }

    @Override
    public String similarMovies(String id, String source) {
        String url = StreamConstants.TMDB_URL + source + "/" + id + "/similar" + StreamConstants.TMDB_API;

        url = url.replace("{key}", tmdbKey);

        SearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(SearchResponse.class).block();

        return new Gson().toJson(res);
    }
}
