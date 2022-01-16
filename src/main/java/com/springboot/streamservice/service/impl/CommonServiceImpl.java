package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.springboot.streamservice.bean.*;
import com.springboot.streamservice.bean.StreamSb.StreamSbFiles;
import com.springboot.streamservice.bean.StreamSb.StreamSbResponse;
import com.springboot.streamservice.bean.tmbdbean.Result;
import com.springboot.streamservice.bean.tmbdbean.TMDBFeaturedResponse;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.CommonDao;
import com.springboot.streamservice.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Value("${streamsb.key}")
    private String streamSbKey;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String featured() {

        List<Featured> featuredList = commonDao.featured();
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
    public ResponseEntity<?> updateFeatured(List<Featured> featuredList) {
        try{

            for (Featured featured :
                    featuredList) {
                String res = commonDao.insertFeatured(featured);
                if ("No Record Updated".equalsIgnoreCase(res) || "Exception Updating Records".equalsIgnoreCase(res)) {
                    return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>("Success", HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>("Failed", HttpStatus.EXPECTATION_FAILED);
        }

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
        res.getResults().retainAll(res.getResults().subList(0,8));
        return new Gson().toJson(res);
    }

    @Override
    public ResponseEntity search(String name, int pageNo) {

        String url = StreamConstants.TMDB_URL + "/search/multi" + StreamConstants.TMDB_API + "&query=" + name + "&page="
                + pageNo;

        url = url.replace("{key}", tmdbKey);

        SearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(SearchResponse.class).block();

        List<Result> removeList = new ArrayList<>();

        for (Result result : res.getResults()) {
            if (null == result.getPoster_path() || (!"movie".equalsIgnoreCase(result.getMedia_type())
                    && !"tv".equalsIgnoreCase(result.getMedia_type()))) {
                removeList.add(result);
            }
        }

        res.getResults().removeAll(removeList);

        res.setTotal_results(res.getResults().size());

        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @Override
    public String moveToDb() {
        String url = StreamConstants.STREAMSB_URL.replace("{key}", streamSbKey);

        StreamSbResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(StreamSbResponse.class).block();

        if (200 == res.status) {
            for (StreamSbFiles files : res.result.files) {

                try {
                    commonDao.moveToDb(files.file_code, files.title);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            return "Successfully Moved data to DB";
        }
        return "Error Moving data to DB";
    }

    @Override
    public String registerUser(UserBean user) {

        if(commonDao.checkIfUserExist(user.getUserName()) < 1){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else {
            return "Account already Exist with this userName : " + user.getUserName();
        }

        return commonDao.insertUser(user);
    }
}
