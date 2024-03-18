package com.example.movieclub.domain.movie;

import com.example.movieclub.domain.genre.Genre;
import com.example.movieclub.domain.genre.GenreRepository;
import com.example.movieclub.domain.movie.dto.MovieDto;
import com.example.movieclub.domain.movie.dto.MovieDtoMapper;
import com.example.movieclub.domain.movie.dto.MovieSaveDto;
import com.example.movieclub.domain.storage.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final FileStorageService fileStorageService;
//    public List<MovieDto> findAllPromotedMovies() {
//        return movieRepository.findAllByPromotedIsTrue()
//                .stream().map(MovieDtoMapper::map)
//                .toList();
//    }
    public Page<MovieDto> findAllPromotedMoviesPaged(PageRequest pageRequest) {
        return movieRepository.findAllByPromotedIsTrue(pageRequest).map(MovieDtoMapper::map);
    }
    public Optional<MovieDto> findMovieById(long id) {
        return movieRepository.findById(id).map(MovieDtoMapper::map);
    }
    public Optional<Movie> findMovieId(Long id) {
        return movieRepository.findById(id);
    }

    public Page<MovieDto> findMoviesByGenreName(String genre, PageRequest request) {
        return movieRepository.findAllByGenre_NameIgnoreCase(genre, request).map(MovieDtoMapper::map);
    }
    public List<MovieDto> findMoviesByGenreName(String genre) {
        return movieRepository.findAllByGenre_NameIgnoreCase(genre).stream().map(MovieDtoMapper::map).toList();
    }

    public void addMovie(MovieSaveDto movieToSave) {
        Movie movie = new Movie();
        movieSaveOrUpdate(movieToSave, movie);
        movieRepository.save(movie);
    }

    public List<MovieDto> findTopMovies(int size) {
        Pageable page = Pageable.ofSize(size);
        return movieRepository.findTopByRating(page).stream()
                .map(MovieDtoMapper::map)
                .toList();
    }
    @Transactional
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("No such film id"));
        movieRepository.deleteById(movie.getId());
    }

    public Page<MovieDto> findAllMovies(PageRequest request) {
        return movieRepository.findAll(request).map(MovieDtoMapper::map);
    }
    public List<MovieDto> findAllMovies() {
        List<Movie> allMovies = movieRepository.findAll();
        List<Movie> movieList = new ArrayList<>(allMovies);
        return movieList.stream().map(MovieDtoMapper::map).toList();
    }

    @Transactional
    public void updateMovieById(Long id, MovieSaveDto movie) {
        Movie movieToUpdate = movieRepository.findById(id).orElseThrow();
        movieSaveOrUpdate(movie, movieToUpdate);
        movieRepository.save(movieToUpdate);
    }

    public void movieSaveOrUpdate(MovieSaveDto movieToSave, Movie movie) {
        movie.setTitle(movieToSave.getTitle());
        movie.setOriginalTitle(movieToSave.getOriginalTitle());
        movie.setPromoted(movieToSave.isPromoted());
        movie.setReleaseYear(movieToSave.getReleaseYear());
        movie.setShortDescription(movieToSave.getShortDescription());
        movie.setDescription(movieToSave.getDescription());
        movie.setYoutubeTrailerId(movieToSave.getYoutubeTrailerId());
        Genre genre = genreRepository.findByNameIgnoreCase(movieToSave.getGenre()).orElseThrow();
        movie.setGenre(genre);
        if (movieToSave.getPoster() != null && !movieToSave.getPoster().isEmpty()) {
            String savedFileName = fileStorageService.saveImage(movieToSave.getPoster());
            movie.setPoster(savedFileName);
        }
    }

    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    public Page<MovieDto> searchMovies(String title, PageRequest request) {
        Page<Movie> listOfMovies;
        if (title == null) {
            listOfMovies = movieRepository.findAll(request);//TODO exception runtime bym zrobil
        } else {
            listOfMovies = movieRepository.findMovieByTitleContainingIgnoreCase(title, request);
        }
        return listOfMovies.map(MovieDtoMapper::map);
    }
}
