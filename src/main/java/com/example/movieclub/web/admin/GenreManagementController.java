package com.example.movieclub.web.admin;

import com.example.movieclub.domain.genre.Genre;
import com.example.movieclub.domain.genre.GenreRepository;
import com.example.movieclub.domain.genre.GenreService;
import com.example.movieclub.domain.genre.dto.GenreDto;
import com.example.movieclub.domain.movie.Movie;
import com.example.movieclub.domain.movie.MovieRepository;
import com.example.movieclub.domain.movie.MovieService;
import com.example.movieclub.domain.movie.dto.MovieDto;
import com.example.movieclub.domain.movie.dto.MovieDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreManagementController {

    private final GenreService genreService;
    private final MovieService movieService;

    @GetMapping("/admin/dodaj-gatunek") // przekierowanie do dodawania gatunku
    public String addGenreForm(Model model) {
        GenreDto genre = new GenreDto();
        model.addAttribute("genre", genre);
        return "admin/genre-form";
    }

    @PostMapping("/admin/dodaj-gatunek")  // dodawanie gatunku
    public String addGenre(GenreDto genre, RedirectAttributes redirectAttributes) {
        genreService.addGenre(genre);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Gatunek %s został zapisany".formatted(genre.getName()));
        return "redirect:/admin";
    }

    @GetMapping("/admin/update-genre/{id}")
    public String manageGenre(@PathVariable Long id, Model model) {
        GenreDto genre = genreService.findGenreById(id).orElseThrow();
        model.addAttribute("genre", genre);
        return "admin/update-genre";
    }

    @PostMapping("/admin/update-genre/{id}")
    public String updateGenre(@PathVariable Long id, GenreDto genreDto, RedirectAttributes redirectAttributes) {
        GenreDto genre = genreService.findGenreById(id).orElseThrow();
        genreService.updateGenre(id, genreDto);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Zaaktualizowano gatunek %s".formatted(genre.getName())
        );
        return "redirect:/admin";
    }

    @GetMapping("/admin/manage-genre")
    public String updateGenreForm(Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        model.addAttribute("genres", allGenres);
        return "admin/manage-genre";
    }

    @PostMapping("/admin/manage-genre/{id}")
    public String deleteGenre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        GenreDto genre = genreService.findGenreById(id).orElseThrow();
        Genre uncategorizedGenre = genreService.findGenreByNameMapped("Uncategorized").orElseThrow(); // wyszukujemy UcategorizedGenre tak aby otrzymac encję
        List<MovieDto> movies = movieService.findMoviesByGenreName(genre.getName());
        for (MovieDto movie : movies) {
            Movie movieToSave = MovieDtoMapper.map(movie); // mapujemy movies na movie
            movieToSave.setGenre(uncategorizedGenre); // ustawiamy genre encji movie
            movieService.save(movieToSave); // zapisujemy encje do bazy
        }
        genreService.deleteGenre(id); // usuwamy genre
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Gatunek %s został usunięty z bazy danych".formatted(genre.getName())
        );
        return "redirect:/admin";
    }
}
