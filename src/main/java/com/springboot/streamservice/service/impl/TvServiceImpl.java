package com.springboot.streamservice.service.impl;

import com.springboot.streamservice.bean.TvEpisodeResponse;
import com.springboot.streamservice.bean.TvSeasonResponse;
import com.springboot.streamservice.bean.tmbdbean.Episode;
import com.springboot.streamservice.bean.tmbdbean.Seasons;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.springboot.streamservice.bean.TvSearchResponse;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.TVService;

import java.util.ArrayList;
import java.util.List;

@Repository
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

		TvSearchResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(TvSearchResponse.class).block();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = "";
		try {
			json = ow.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public String getTvById(String id) {

		String apiKey = StreamConstants.TMDB_API;
		apiKey = apiKey.replace("{key}", tmdbKey);

		String url = StreamConstants.TMDB_URL + "tv/"+id+ apiKey;


		TvSeasonResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(TvSeasonResponse.class).block();

		TvEpisodeResponse ep = new TvEpisodeResponse();

//		List<Episode> episodes = new ArrayList<>();
//		episodes.add(ep);

		for(Seasons season : res.getSeasons()){
			String seasonNo = Integer.toString(season.getSeason_number());
			String getEpUrl = StreamConstants.TMDB_URL + "tv/" + id + "/season/" + seasonNo + apiKey;
			ep = WebClient.create().get().uri(getEpUrl).retrieve().bodyToMono(TvEpisodeResponse.class).block();

			season.setEpisodes(ep.getEpisodes());

			for(Episode episode : season.getEpisodes()){
				String episodeNo = Integer.toString(episode.getEpisode_number());
//				https://api.themoviedb.org/3/tv/2316/season/1/episode/1/external_ids?api_key=d7f6cbb170a30076b40db652d7ea26fc
			}
		}

//		for (int i = 0; i < res.getSeasons().size(); i++) {
//			String seasonNo = Integer.toString(res.getSeasons().get(i).getSeason_number());
//			String getEpUrl = StreamConstants.TMDB_URL + "tv/" + id + "/season/" + seasonNo + apiKey;
//			ep = WebClient.create().get().uri(getEpUrl).retrieve().bodyToMono(TvEpisodeResponse.class).block();
//			res.seasons.get(i).setEpisodes(ep.getEpisodes());
//		}

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = "";
		try {
			json = ow.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}

//	@Override
//	public String getEpisode(String id, String season) {
//		String url = StreamConstants.TMDB_URL + "/tv/"+id+"/season/"+season+ StreamConstants.TMDB_API;
//
//		url = url.replace("{key}", tmdbKey);
//
//		TvEpisodeResponse res = WebClient.create().get().uri(url).retrieve().bodyToMono(TvEpisodeResponse.class).block();
//
//		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//		String json = "";
//		try {
//			json = ow.writeValueAsString(res);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//
//		return json;
//	}

}
