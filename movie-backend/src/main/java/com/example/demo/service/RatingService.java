package com.example.demo.service;

import com.example.demo.dto.RatingRequest;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Rating;
import com.example.demo.entity.User;
import com.example.demo.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieService movieService;
    private final UserService userService;

    public RatingService(RatingRepository ratingRepository, MovieService movieService, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.movieService = movieService;
        this.userService = userService;
    }

    @Transactional
    public Rating rateMovie(RatingRequest request, String username) {
        Movie movie = movieService.getMovieById(request.getMovieId()).orElseThrow();
        User user = userService.findByUsername(username);

        return ratingRepository.findByUserAndMovie(user, movie)
                .map(existingRating -> {
                    if (existingRating.getIsDeleted()) {
                        // Reactivate soft-deleted rating
                        existingRating.setIsDeleted(false);
                    }
                    existingRating.setRating(request.getRating());
                    return ratingRepository.save(existingRating);
                })
                .orElseGet(() -> {
                    Rating newRating = Rating.builder()
                            .movie(movie)
                            .user(user)
                            .rating(request.getRating())
                            .isDeleted(false)
                            .build();
                    return ratingRepository.save(newRating);
                });
    }

    public Rating getMovieRating(Long movieId, String username) {
        Movie movie = movieService.getMovieById(movieId).orElseThrow();
        User user = userService.findByUsername(username);

        return ratingRepository.findByUserAndMovieAndIsDeletedFalse(user, movie)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
    }

    public List<Rating> getMovieRatings(Long movieId) {
        Movie movie = movieService.getMovieById(movieId).orElseThrow();
        return ratingRepository.findByMovieAndIsDeletedFalse(movie);
    }

    public Double getAverageRating(Long movieId) {
        Movie movie = movieService.getMovieById(movieId).orElseThrow();
        return ratingRepository.calculateAverageRatingByMovie(movie);
    }

    @Transactional
    public void deleteRating(Long movieId, String username) {
        User user = userService.findByUsername(username);
        Movie movie = movieService.getMovieById(movieId).orElseThrow();

        Rating rating = ratingRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        if (!rating.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own rating");
        }

        ratingRepository.softDelete(rating.getId(), user);
    }

    @Transactional
    public void updateRating(RatingRequest request, String username) {
        User user = userService.findByUsername(username);
        Movie movie = movieService.getMovieById(request.getMovieId()).orElseThrow();

        Rating rating = ratingRepository.findByUserAndMovieAndIsDeletedFalse(user, movie)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        rating.setRating(request.getRating());
        ratingRepository.save(rating);
    }
}