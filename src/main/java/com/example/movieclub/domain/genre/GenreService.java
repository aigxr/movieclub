package com.example.movieclub.domain.genre;

import com.example.movieclub.domain.genre.dto.GenreDto;
import com.example.movieclub.domain.genre.dto.GenreDtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    public Optional<GenreDto> findGenreByName(String name) {
        return genreRepository.findByNameIgnoreCase(name).map(GenreDtoMapper::map);
    }

    public Optional<Genre> findGenreByNameMapped(String name) {
        return genreRepository.findByNameIgnoreCase(name);
    }

    public Optional<GenreDto> findGenreById(Long id) {
        return genreRepository.findGenreById(id).map(GenreDtoMapper::map);
    }

    public List<GenreDto> findAllGenres() {
        return StreamSupport.stream(genreRepository.findAll().spliterator(), false)
                .map(GenreDtoMapper::map)
                .toList();
    }

    @Transactional
    public void addGenre(GenreDto genre) {
        Genre genreToSave = new Genre();
        genreToSave.setName(genre.getName());
        genreToSave.setDescription(genre.getDescription());
        genreRepository.save(genreToSave);
    }

    @Transactional
    public void updateGenre(Long id, GenreDto genreDto) {
        Genre genre = genreRepository.findGenreById(id).orElseThrow();
        genre.setName(genreDto.getName());
        genre.setDescription(genreDto.getDescription());
        genreRepository.save(genre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
