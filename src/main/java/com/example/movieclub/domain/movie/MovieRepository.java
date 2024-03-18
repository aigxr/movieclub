package com.example.movieclub.domain.movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findAllByPromotedIsTrue(PageRequest pageRequest);
    Page<Movie> findAllByGenre_NameIgnoreCase(String genre, PageRequest request);
    List<Movie> findAllByGenre_NameIgnoreCase(String genre);
    @Query("SELECT m FROM Movie m join m.ratings r group by m order by avg(r.rating) desc")
    List<Movie> findTopByRating(Pageable page);
    Page<Movie> findMovieByTitleContainingIgnoreCase(String title, PageRequest request);
    List<Movie> findMovieByOriginalTitleContainingIgnoreCase(String originalTitle);
}
