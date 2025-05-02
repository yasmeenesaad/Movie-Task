package com.example.demo.controller;

import com.example.demo.dto.MovieRequest;
import com.example.demo.entity.Movie;
import com.example.demo.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<Page<Movie>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(movieService.getAllMovies(PageRequest.of(page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String genre) {
        return ResponseEntity.ok(movieService.searchMovies(title, year, genre));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.addMovie(request));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie marked as deleted successfully");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/batch-add")
    public ResponseEntity<List<Movie>> batchAddMovies(@RequestBody List<MovieRequest> movieRequests) {
        return ResponseEntity.ok(movieService.batchAddMovies(movieRequests));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/batch-delete")
    public ResponseEntity<Void> batchDeleteMovies(@RequestBody List<Long> movieIds) {
        movieService.batchDeleteMovies(movieIds);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{movieId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long movieId) {
        System.out.println("Fetching average rating for movie: " + movieId);
        Double avg = movieService.getAverageRating(movieId);
        return ResponseEntity.ok(avg != null ? avg : 0.0);
    }

}