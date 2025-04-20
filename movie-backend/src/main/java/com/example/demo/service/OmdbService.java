package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class OmdbService {

    @Value("${omdb.api.key}")
    private String apiKey;

    @Autowired
    private MovieService movieService;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String OMDB_API_URL = "https://www.omdbapi.com/?apikey=";

    // 🔍 Search movies by title
    public List<Map<String, Object>> searchMovies(String title) {
        String url = OMDB_API_URL + apiKey + "&s=" + title;
        Map response = restTemplate.getForObject(url, Map.class);
        return (List<Map<String, Object>>) response.get("Search");
    }



    // 🎬 Get movie details by IMDb ID
    public Map<String, Object> getMovieDetails(long id) {
        String url = OMDB_API_URL + apiKey + "&i=" + movieService.getMovieById(id).orElseThrow().getImdbId();
        return restTemplate.getForObject(url, Map.class);
    }
}