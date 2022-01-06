package com.springboot.streamservice.constants;

public class StreamConstants {

    // Stream Tape
    public static final String STREAMTAPE_URL = "https://api.streamtape.com";
    public static final String STREAMTAPE_MOVIE_URL = "https://streamta.pe/e/";
    public static final String LIST_FILES = "/file/listfolder";
    public static final String LOGIN = "?login={login}&key={key}&folder={folder}";

    // StreamSB
    public static final String STREAMSB_URL = "https://api.streamsb.com/api/file/list?key={key}";
    public static final String STREAMSB_WATCH_URL = "https://watchsb.com/e/";

    // TMDB
    public static final String TMDB_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_API = "?api_key={key}";

    // STREAM SERVER
    public static final String SERVER_URL = "https://www.2embed.ru/embed/imdb/movie?id={imdb}";
    //"https://vidclouds.us/{imdb}.html";
    public static final String SERVER_TV_URL = "https://www.2embed.ru/embed/imdb/tv?id={imdb}&s={season}&e={episode}";
    //"https://vidclouds.us/tv.php?imdb={imdb}&season={season}&episode={episode}";

    public static final String MOVIE = "movie";
    public static final String TV = "tv";
    public static final String INSERT = "insert";
    public static final String DELETE = "delete";
}
