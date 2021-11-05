package com.springboot.streamservice.constants;

public class StreamConstants {

	// Stream Tape
	public static final String STREAMTAPE_URL = "https://api.streamtape.com";
	public static final String STREAMTAPE_MOVIE_URL = "https://streamta.pe/e/";
	public static final String LIST_FILES = "/file/listfolder";
	public static final String LOGIN = "?login={login}&key={key}&folder={folder}";
	
	// TMDB
	public static final String TMDB_URL = "https://api.themoviedb.org/3/";
	public static final String TMDB_API = "?api_key={key}";
	public static final String TMDB_PAGE = "&page=";

	// vidcloud
	public static final String VIDCLOUD_URL = "https://vidclouds.us/{imdb}.html";
	public static final String VIDCLOUD_TV_URL = "https://vidclouds.us/tv.php?imdb={imdb}&season={season}&episode={episode}";
	
	
}
