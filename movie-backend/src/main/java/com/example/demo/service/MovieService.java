package com.example.demo.service;

import com.example.demo.dto.MovieRequest;
import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByIsDeletedFalse();
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAllByIsDeletedFalse(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))));
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findByIdAndIsDeletedFalse(id);
    }

    public Optional<Movie> getMovieByTitle(String title) {
        return movieRepository.findByTitleAndIsDeletedFalse(title);
    }

    public List<Movie> searchMovies(String title, String year, String genre) {
        if (title != null && !title.isEmpty()) {
            return movieRepository.searchByTitleAndIsDeletedFalse(title);
        } else if (year != null && !year.isEmpty()) {
            return movieRepository.findByYearAndIsDeletedFalse(year);
        } else if (genre != null && !genre.isEmpty()) {
            return movieRepository.findByGenreContainingIgnoreCaseAndIsDeletedFalse(genre);
        }
        return movieRepository.findAllByIsDeletedFalse();
    }

    @Transactional
    public Movie addMovie(MovieRequest movieRequest) {
        // Check if movie with same imdbId exists (soft deleted or not)
        Optional<Movie> existingMovie = movieRepository.findByImdbId(movieRequest.getImdbId());

        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            if (movie.getIsDeleted()) {
                // Reactivate soft-deleted movie
                movie.setIsDeleted(false);
                movie.setTitle(movieRequest.getTitle());
                movie.setDirector(movieRequest.getDirector());
                movie.setPlot(movieRequest.getPlot());
                movie.setGenre(movieRequest.getGenre());
                movie.setYear(movieRequest.getYear());
                movie.setPoster(movieRequest.getPoster());
                return movieRepository.save(movie);
            }
            // If not deleted, return existing movie (idempotent behavior)
            return movie;
        }

        // Create new movie
        Movie movie = new Movie();
        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setPlot(movieRequest.getPlot());
        movie.setGenre(movieRequest.getGenre());
        movie.setYear(movieRequest.getYear());
        movie.setImdbId(movieRequest.getImdbId());
        movie.setPoster(movieRequest.getPoster());
        movie.setIsDeleted(false);
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.findById(id).ifPresent(movie -> {
            movie.setIsDeleted(true);
            movieRepository.save(movie);
        });
    }

    public List<Movie> batchAddMovies(List<MovieRequest> movieRequests) {
        List<Movie> movies = movieRequests.stream()
                .map(request -> {
                    Optional<Movie> existing = movieRepository.findByImdbId(request.getImdbId());
                    if (existing.isPresent()) {
                        Movie movie = existing.get();
                        if (movie.getIsDeleted()) {
                            movie.setIsDeleted(false);
                            movie.setTitle(request.getTitle());
                            movie.setDirector(request.getDirector());
                            movie.setYear(request.getYear());
                            movie.setGenre(request.getGenre());
                            movie.setPlot(request.getPlot());
                            movie.setPoster(request.getPoster());
                        }
                        return movie;
                    }
                    return new Movie(
                            request.getTitle(),
                            request.getDirector(),
                            request.getYear(),
                            request.getGenre(),
                            request.getPlot(),
                            request.getImdbId(),
                            request.getPoster()
                    );
                })
                .collect(Collectors.toList());

        return movieRepository.saveAll(movies);
    }

    public void batchDeleteMovies(List<Long> movieIds) {
        List<Movie> movies = movieRepository.findAllById(movieIds);
        movies.forEach(movie -> movie.setIsDeleted(true));
        movieRepository.saveAll(movies);
    }
}