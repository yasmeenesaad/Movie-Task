package com.example.demo.service;



import com.example.demo.dto.RatingRequest;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Rating;
import com.example.demo.entity.User;
import com.example.demo.repository.RatingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    public Rating rateMovie(RatingRequest request, String username) {
        Movie movie = movieService.getMovieById(request.getMovieId()).orElseThrow();
        User user = userService.findByUsername(username);

        return ratingRepository.findByUserAndMovie(user,movie)
                .map(existingRating -> {
                    existingRating.setRating(request.getRating());
                    return ratingRepository.save(existingRating);
                })
                .orElseGet(() -> {
                    Rating newRating = new Rating();
                    newRating.setMovie(movie);
                    newRating.setUser(user);
                    newRating.setRating(request.getRating());
                    return ratingRepository.save(newRating);
                });
    }

    public Rating getMovieRating(Long movieId, String username) {
        Movie movie = movieService.getMovieById(movieId).orElseThrow();
        User user = userService.findByUsername(username);

        return ratingRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
    }

    public List<Rating> getMovieRatings(Long movieId) {
        Optional<Movie> movie = movieService.getMovieById(movieId);
        return ratingRepository.findByMovie(movie.orElse(null));
    }

    public void deleteRating(Long movieId, String username) {
        Rating rating = ratingRepository.findByUserAndMovie(userService.findByUsername(username), movieService.getMovieById(movieId).orElseThrow())
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        if (!rating.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own rating");
        }

        ratingRepository.delete(rating);
    }

    public void updateRating(RatingRequest request, String username) {
        User user = userService.findByUsername(username);
        Movie movie = movieService.getMovieById(request.getMovieId()).orElseThrow();

        ratingRepository.findByUserAndMovie(userService.findByUsername(username), movieService.getMovieById(request.getMovieId()).orElseThrow())
                .map(existingRating -> {
                    existingRating.setRating(request.getRating());
                    return ratingRepository.save(existingRating);
                })
                .orElseThrow(() -> new RuntimeException("Rating not found"));
    }
}