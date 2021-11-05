package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.springboot.streamservice.bean.StreamTapeResponse;
import com.springboot.streamservice.bean.TvEpisodeResponse;
import com.springboot.streamservice.bean.TvSeasonResponse;
import com.springboot.streamservice.bean.tmbdbean.Episode;
import com.springboot.streamservice.bean.tmbdbean.Seasons;
import com.springboot.streamservice.bean.tmbdbean.StreamTapeFile;
import com.springboot.streamservice.dao.StreamTapeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.springboot.streamservice.bean.TvSearchResponse;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.TVService;

@Service
public class TvServiceImpl implements TVService {
	
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
	public String searchTVByName(String name, int pageNo) {

		String url = StreamConstants.TMDB_URL + "/search/tv" + StreamConstants.TMDB_API + "&query=" + name + "&page="
				+ pageNo;

		url = url.replace("{key}", tmdbKey);

		TvSearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(TvSearchResponse.class).block();

		return new Gson().toJson(res);
	}

	@Override
	public String getTvById(String id) {

		String apiKey = StreamConstants.TMDB_API;
		apiKey = apiKey.replace("{key}", tmdbKey);

		String url = StreamConstants.TMDB_URL + "tv/" + id + apiKey;

		TvSeasonResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(TvSeasonResponse.class).block();

		String showImdbIdUrl = StreamConstants.TMDB_URL + "tv/" + id + "/external_ids" + apiKey;
		String showJson = WebClient.create().get().uri(showImdbIdUrl).retrieve().bodyToMono(String.class).block();
		JsonObject showImdbJson = new Gson().fromJson(showJson, JsonObject.class);
		res.setImdbId(showImdbJson.get("imdb_id").getAsString());

		TvEpisodeResponse ep;

		for(Seasons season : res.getSeasons()){
			String seasonNo = Integer.toString(season.getSeason_number());
			String getEpUrl = StreamConstants.TMDB_URL + "tv/" + id + "/season/" + seasonNo + apiKey;
			ep = WebClient.create().get().uri(getEpUrl).retrieve().bodyToMono(TvEpisodeResponse.class).block();

			season.setEpisodes(ep.getEpisodes());

			for(Episode episode : season.getEpisodes()){
				String episodeNo = Integer.toString(episode.getEpisode_number());

				String epImdbId = StreamConstants.TMDB_URL + "tv/" + id + "/season/" + seasonNo
						+ "/episode/" + episodeNo + "/external_ids" + apiKey;

				String json = WebClient.create().get().uri(epImdbId).retrieve().bodyToMono(String.class).block();

				JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);

				if(!"null".equalsIgnoreCase(convertedObject.get("imdb_id").toString())) {
					String imdbId = convertedObject.get("imdb_id").getAsString();
//					episode.setUrl(streamTapeDAO.generateUrl(imdbId, showImdbIdUrl));

					String tvUrl = StreamConstants.VIDCLOUD_TV_URL.replace("{imdb}", res.getImdbId())
							.replace("{season}", String.valueOf(episode.getSeason_number()))
							.replace("{episode}", String.valueOf(episode.getEpisode_number()));

					episode.setUrl(tvUrl);
				}else {
					episode.setStatus("Not Avaliable");
				}
			}
		}

		return new Gson().toJson(res);
	}

}
