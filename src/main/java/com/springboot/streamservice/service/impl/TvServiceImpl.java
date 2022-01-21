package com.springboot.streamservice.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.springboot.streamservice.bean.MovieDbBean;
import com.springboot.streamservice.bean.SearchResponse;
import com.springboot.streamservice.bean.TvEpisodeResponse;
import com.springboot.streamservice.bean.TvSeasonResponse;
import com.springboot.streamservice.bean.tmbdbean.Episode;
import com.springboot.streamservice.bean.tmbdbean.Seasons;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.dao.CommonDao;
import com.springboot.streamservice.service.TVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class TvServiceImpl implements TVService {

	@Value("${tmdb.key}")
	private String tmdbKey;

	@Autowired
	private CommonDao commonDao;

	@Override
	public String getTvById(String id) {

		String apiKey = StreamConstants.TMDB_API;
		apiKey = apiKey.replace("{key}", tmdbKey);

		String url = StreamConstants.TMDB_URL + "tv/" + id + apiKey;

		TvSeasonResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(TvSeasonResponse.class).block();

		JsonObject showImdbJson = getShowImdbId(id, apiKey);
		res.setImdbId(showImdbJson.get("imdb_id").getAsString());

		if(res.getSeasons().get(0).getSeason_number() == 0){
			res.getSeasons().remove(0);
		}

		Seasons season = res.getSeasons().get((res.getSeasons().get(0).getSeason_number() == 0)?1:0);
		String seasonNo = Integer.toString(season.getSeason_number());
		TvEpisodeResponse ep = getTvEpisodeResponse(id, apiKey, seasonNo);

		season.setEpisodes(ep.getEpisodes());

		setEpisodes(id, seasonNo, apiKey, ep, res.getImdbId());

		return new Gson().toJson(res);
	}

	private TvEpisodeResponse getTvEpisodeResponse(String id, String apiKey, String seasonNo) {
		TvEpisodeResponse ep;
		String getEpUrl = StreamConstants.TMDB_URL + "tv/" + id + "/season/" + seasonNo + apiKey;
		ep = WebClient.create().get().uri(getEpUrl).retrieve().bodyToMono(TvEpisodeResponse.class).block();
		return ep;
	}

	private JsonObject getShowImdbId(String id, String apiKey) {
		String showImdbIdUrl = StreamConstants.TMDB_URL + "tv/" + id + "/external_ids" + apiKey;
		String showJson = WebClient.create().get().uri(showImdbIdUrl).retrieve().bodyToMono(String.class).block();
		return new Gson().fromJson(showJson, JsonObject.class);
	}

	@Override
	public String getEpisodes(String id, String seasonNo) {
		String apiKey = StreamConstants.TMDB_API;
		apiKey = apiKey.replace("{key}", tmdbKey);

		TvEpisodeResponse ep = getTvEpisodeResponse(id, apiKey, seasonNo);

		JsonObject showImdbJson = getShowImdbId(id, apiKey);

		String showIMDBId = showImdbJson.get("imdb_id").getAsString();

		setEpisodes(id, seasonNo, apiKey, ep, showIMDBId);

		return new Gson().toJson(ep);
	}

	private void setEpisodes(String id, String seasonNo, String apiKey, TvEpisodeResponse ep, String showIMDBId) {
		for(Episode episode : ep.getEpisodes()){
			String episodeNo = Integer.toString(episode.getEpisode_number());

			String epImdbId = StreamConstants.TMDB_URL + "tv/" + id + "/season/" + seasonNo
					+ "/episode/" + episodeNo + "/external_ids" + apiKey;

			String json = WebClient.create().get().uri(epImdbId).retrieve().bodyToMono(String.class).block();

			JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);

			if(!"null".equalsIgnoreCase(convertedObject.get("imdb_id").toString())) {
				String imdbId = convertedObject.get("imdb_id").getAsString();
				episode.setImdbId(imdbId);
				episode.setUrl(generateTvUrl(imdbId, showIMDBId, episode.getSeason_number(), episode.getEpisode_number()));
			}else {
				episode.setStatus("Not Avaliable");
			}
		}
	}


	public List<String> generateTvUrl(String imdbId, String showImdbIdUrl, int season_number, int episode_number) {
		List<String> list = new ArrayList<>();
		try {
			List<MovieDbBean> movieDbBeans = commonDao.findByImdbId(imdbId);

			for (MovieDbBean file : movieDbBeans) {
				if (file.getImdbid().equalsIgnoreCase(imdbId)) {
					list.add(StreamConstants.STREAMSB_WATCH_URL + file.getUrl());
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}

		list.add(StreamConstants.SERVER_TV_URL.replace("{imdb}", showImdbIdUrl)
				.replace("{season}", String.valueOf(season_number))
				.replace("{episode}", String.valueOf(episode_number)));

		return list;
	}

}
