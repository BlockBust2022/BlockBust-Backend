package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.springboot.streamservice.bean.*;
import com.springboot.streamservice.bean.tmbdbean.Episode;
import com.springboot.streamservice.bean.tmbdbean.Result;
import com.springboot.streamservice.bean.tmbdbean.Seasons;
import com.springboot.streamservice.bean.tmbdbean.StreamTapeFile;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.TVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

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
	
	@Override
	public String searchTVByName(String name, int pageNo) {

		String url = StreamConstants.TMDB_URL + "/search/tv" + StreamConstants.TMDB_API + "&query=" + name + "&page="
				+ pageNo;

		url = url.replace("{key}", tmdbKey);

		SearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(SearchResponse.class).block();

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
					episode.setUrl(generateTvUrl(imdbId, res.getImdbId(), episode.getSeason_number(), episode.getEpisode_number()));
				}else {
					episode.setStatus("Not Avaliable");
				}
			}
		}

		return new Gson().toJson(res);
	}


	public String generateTvUrl(String imdbId, String showImdbIdUrl, int season_number, int episode_number) {
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

		return StreamConstants.SERVER_TV_URL.replace("{imdb}", showImdbIdUrl)
				.replace("{season}", String.valueOf(season_number))
				.replace("{episode}", String.valueOf(episode_number));
	}

}
