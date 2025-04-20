package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String year;
    private String genre;
    private String director;
    private String plot;
    private String poster;
    @Column(nullable = false, unique = true)
    private String imdbId;

    public Movie(String title, String director, String year, String genre, String plot, String imdbId, String poster) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.genre = genre;
        this.plot = plot;
        this.imdbId = imdbId;
        this.poster = poster;
    }
}
