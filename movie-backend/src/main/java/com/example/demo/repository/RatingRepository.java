package com.example.demo.repository;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Rating;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovie(Movie movie);
    List<Rating> findByUser(User user);
    Optional<Rating> findByUserAndMovie(User user, Movie movie);
}
