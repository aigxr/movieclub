package com.example.movieclub.domain.genre.dto;

import com.example.movieclub.domain.genre.Genre;

public class GenreDtoMapper { //@Mapper
    public static GenreDto map(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName(),
                genre.getDescription()
        );
    }
}
