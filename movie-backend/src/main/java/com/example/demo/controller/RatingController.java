package com.example.demo.controller;

import com.example.demo.dto.RatingRequest;
import com.example.demo.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<?> rateMovie(@RequestBody RatingRequest request, Authentication authentication) {
        return ResponseEntity.ok(ratingService.rateMovie(request, authentication.getName()));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<?> getMovieRating(@PathVariable Long movieId, Authentication authentication) {
        return ResponseEntity.ok(ratingService.getMovieRating(movieId, authentication.getName()).getRating());
    }

    @GetMapping("/average/{movieId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long movieId) {
        return ResponseEntity.ok(ratingService.getAverageRating(movieId));
    }

    @PutMapping
    public ResponseEntity<?> updateRating(@RequestBody RatingRequest request, Authentication authentication) {
        ratingService.updateRating(request, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteRating(@PathVariable Long movieId, Authentication authentication) {
        ratingService.deleteRating(movieId, authentication.getName());
        return ResponseEntity.ok().build();
    }
}