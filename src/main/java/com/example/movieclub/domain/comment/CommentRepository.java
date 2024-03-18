package com.example.movieclub.domain.comment;

import com.example.movieclub.domain.user.dto.UserCredentialsDto;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findCommentsByMovieId(Long movieId);
    Comment findCommentByUserId(Long userId);
    int countByUserIdAndDateAfter(Long id, LocalDateTime date);
    void deleteCommentByMovieId(Long id);
    void deleteCommentByUserId(Long id);
}
