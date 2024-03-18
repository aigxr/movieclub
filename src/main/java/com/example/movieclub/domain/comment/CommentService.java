package com.example.movieclub.domain.comment;

import com.example.movieclub.domain.comment.dto.CommentDto;
import com.example.movieclub.domain.movie.Movie;
import com.example.movieclub.domain.movie.MovieService;
import com.example.movieclub.domain.user.User;
import com.example.movieclub.domain.user.UserService;
import com.example.movieclub.domain.comment.dto.CommentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MovieService movieService;
    private final UserService userService;
    public void addCommentByMovieId(Long id, String userEmail, CommentDto commentDto) { //TODO dodac mozliwosc usuwanioa komentarzy
        Comment comment = new Comment();
        LocalDateTime now = LocalDateTime.now();
        Movie movie = movieService.findMovieId(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + id));
        User user = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + userEmail));
        comment.setUserComment(commentDto.getUserComment());
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setDate(now);
        commentRepository.save(comment);
    }

    public List<CommentDto> findAllCommentsByMovieId(Long movieId) {
        return commentRepository.findCommentsByMovieId(movieId).stream().map(CommentDtoMapper::map).toList();
    }

    public List<CommentDto> findAllComments() {
        Iterable<Comment> foundComments = commentRepository.findAll();
        List<Comment> comments = new ArrayList<>();
        for (Comment foundComment : foundComments) {
            comments.add(foundComment);
        }
        return comments.stream().map(CommentDtoMapper::map).toList();
    }

    public Comment findCommentByUserId(Long userId) {
        return commentRepository.findCommentByUserId(userId);
    }

    public void deleteCommentByMovieId(Long id) {
        commentRepository.deleteCommentByMovieId(id);
    }
    public void deleteCommentByUserId(Long id) {
        commentRepository.deleteCommentByUserId(id);
    }

    public boolean countCommentsByUserAndDate(Long id) {
        LocalDateTime date = LocalDateTime.now().minusMinutes(5);
        int count = commentRepository.countByUserIdAndDateAfter(id, date);
        System.out.println(count);
        return count > 3;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
