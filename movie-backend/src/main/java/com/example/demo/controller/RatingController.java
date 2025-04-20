package com.example.demo.controller;


import com.example.demo.dto.RatingRequest;
import com.example.demo.entity.Rating;
import com.example.demo.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Rating> rateMovie(@RequestBody RatingRequest request, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(ratingService.rateMovie(request, username));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<Integer> getMovieRating(@PathVariable Long movieId, Authentication authentication) {
        return ResponseEntity.ok(ratingService.getMovieRating(movieId, authentication.getName()).getRating());
    }

    @PutMapping()
    public void updateRating( @RequestBody RatingRequest request, Authentication authentication) {
        String username = authentication.getName();
        ratingService.updateRating(request, username);
    }

    @DeleteMapping("/{movieId}")
    public void deleteRating(@PathVariable Long movieId, Authentication authentication) {
        String username = authentication.getName();
        ratingService.deleteRating(movieId, username);
    }
}
