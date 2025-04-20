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
        return movieRepository.findAll();
    }

    public Page<Movie> getAllMovies(Pageable pageable){

        return movieRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))));
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Optional<Movie> getMovieByTitle(String title) {
        return movieRepository.findByTitle(title);
    }


    public List<Movie> searchMovies(String title, String year, String genre) {
        if (title != null && !title.isEmpty()) {
            return movieRepository.searchByTitle(title);
        } else if (year != null && !year.isEmpty()) {
            return movieRepository.findByYear(year);
        } else if (genre != null && !genre.isEmpty()) {
            return movieRepository.findByGenreContainingIgnoreCase(genre);
        }
        return movieRepository.findAll();
    }

    @Transactional
    public Movie addMovie(MovieRequest movieRequest) {
        Movie movie = new Movie();
        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setPlot(movieRequest.getPlot());
        movie.setGenre(movieRequest.getGenre());
        movie.setYear(movieRequest.getYear());
        movie.setImdbId(movieRequest.getImdbId());
        movie.setPoster(movieRequest.getPoster());
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }


    public List<Movie> batchAddMovies(List<MovieRequest> movieRequests) {
        List<Movie> movies = movieRequests.stream()
                .map(request -> new Movie(request.getTitle(), request.getDirector(), request.getYear(), request.getGenre(), request.getPlot(), request.getImdbId(), request.getPoster()))
                .collect(Collectors.toList());

        return movieRepository.saveAll(movies);
    }

    public void batchDeleteMovies(List<Long> movieIds) {
        movieRepository.deleteAllById(movieIds);
    }
}