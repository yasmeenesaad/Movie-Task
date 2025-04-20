package com.example.demo.controller;



import com.example.demo.dto.MovieRequest;
import com.example.demo.entity.Movie;
import com.example.demo.service.MovieService;
import com.example.demo.service.OmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/omdb")
public class OmdbController {

    private final OmdbService omdbService;
    private final MovieService movieService;

    public OmdbController(OmdbService omdbService, MovieService movieService) {
        this.omdbService = omdbService;
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchMovies(@RequestParam String title) {
        List<Map<String, Object>> movies = omdbService.searchMovies(title);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getMovieDetails(@RequestParam long id) {
        Map<String, Object> movieDetails = omdbService.getMovieDetails(id);
        return ResponseEntity.ok(movieDetails);
    }

    @PostMapping("/add")
    public ResponseEntity<Movie> addMovie(@RequestBody MovieRequest movieRequest) {
        Movie movie = movieService.addMovie(movieRequest);
        return ResponseEntity.ok(movie);
    }
}