package com.example.movieclub.domain.movie.dto;

import com.example.movieclub.domain.genre.Genre;
import com.example.movieclub.domain.movie.Movie;
import com.example.movieclub.domain.rating.Rating;

import java.util.Set;

public class MovieDtoMapper {
    public static MovieDto map(Movie movie) {
        double avgRating = movie.getRatings().stream()
                .map(Rating::getRating)
                .mapToDouble(val -> val)
                .average().orElse(0);
        int ratingCount = movie.getRatings().size();
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getOriginalTitle(),
                movie.getShortDescription(),
                movie.getDescription(),
                movie.getYoutubeTrailerId(),
                movie.getReleaseYear(),
                movie.getGenre().getName(),
                movie.isPromoted(),
                movie.getPoster(),
                avgRating,
                ratingCount
        );
    }
    public static Movie map(MovieDto movieDto) {
        Genre genre = new Genre();
        genre.setName(movieDto.getGenre());
        Movie movie = new Movie();
        Set<Rating> ratings = movie.getRatings();
        movie.setId(movieDto.getId());
        movie.setTitle(movieDto.getTitle());
        movie.setOriginalTitle(movieDto.getOriginalTitle());
        movie.setShortDescription(movieDto.getShortDescription());
        movie.setDescription(movieDto.getDescription());
        movie.setYoutubeTrailerId(movieDto.getYoutubeTrailerId());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setGenre(genre);
        movie.setRatings(ratings);
        movie.setPromoted(movieDto.isPromoted());
        movie.setPoster(movieDto.getPoster());
        return movie;
    }
}
