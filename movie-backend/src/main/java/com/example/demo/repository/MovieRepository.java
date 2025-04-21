package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Standard find methods with isDeleted=false filter
    Optional<Movie> findByIdAndIsDeletedFalse(Long id);
    Optional<Movie> findByTitleAndIsDeletedFalse(String title);
    Optional<Movie> findByImdbId(String imdbId); // Needed for idempotent checks (includes deleted records)
    List<Movie> findAllByIsDeletedFalse();
    Page<Movie> findAllByIsDeletedFalse(Pageable pageable);

    // Search methods with isDeleted=false filter
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')) AND m.isDeleted = false")
    List<Movie> searchByTitleAndIsDeletedFalse(String title);

    List<Movie> findByYearAndIsDeletedFalse(String year);
    List<Movie> findByGenreContainingIgnoreCaseAndIsDeletedFalse(String genre);

    // Original methods kept for internal use (may return deleted records)
    Optional<Movie> findByTitle(String title);
    List<Movie> findByYear(String year);

    // Bulk soft delete operation
    @Transactional
    @Modifying
    @Query("UPDATE Movie m SET m.isDeleted = true WHERE m.id IN :ids")
    void softDeleteAllByIdIn(List<Long> ids);
}