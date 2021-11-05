package com.springboot.streamservice.dao.impl;

import com.springboot.streamservice.bean.StreamTapeResponse;
import com.springboot.streamservice.bean.tmbdbean.StreamTapeFile;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.StreamTapeDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
public class StreamTapeDAOImpl implements StreamTapeDAO {

    @Value("${streamtape.login}")
    private String login;

    @Value("${streamtape.key}")
    private String key;

    @Value("${tmdb.key}")
    private String tmdbKey;

    @Value("${streamtape.folder}")
    private String folder;

    @Override
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
