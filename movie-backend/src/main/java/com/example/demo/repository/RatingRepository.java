package com.example.demo.repository;

import com.example.demo.entity.Movie;
import com.example.demo.entity.Rating;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieAndIsDeletedFalse(Movie movie);
    List<Rating> findByUserAndIsDeletedFalse(User user);
    Optional<Rating> findByUserAndMovieAndIsDeletedFalse(User user, Movie movie);

    // For internal use (includes deleted ratings)
    Optional<Rating> findByUserAndMovie(User user, Movie movie);

    @Transactional
    @Modifying
    @Query("UPDATE Rating r SET r.isDeleted = true WHERE r.id = :id AND r.user = :user")
    void softDelete(Long id, User user);

//    COALESCE returns the first non-null value
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Rating r WHERE r.movie = :movie AND r.isDeleted = false")
    Double calculateAverageRatingByMovie(Movie movie);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);
}