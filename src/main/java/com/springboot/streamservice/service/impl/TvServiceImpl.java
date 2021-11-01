package com.springboot.streamservice.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.springboot.streamservice.bean.TvSearchResponse;
import com.springboot.streamservice.constants.StreamConstants;
import com.springboot.streamservice.service.TVService;

@Component
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

}
