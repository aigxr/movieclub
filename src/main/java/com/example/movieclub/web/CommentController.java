package com.example.movieclub.web;

import com.example.movieclub.domain.comment.CommentService;
import com.example.movieclub.domain.comment.dto.CommentDto;
import com.example.movieclub.domain.user.User;
import com.example.movieclub.domain.user.UserService;
import com.example.movieclub.web.admin.AdminController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/film/add-comment/{id}")
    public String addComment(@PathVariable Long id, CommentDto comment, Authentication authentication) {
        User user = userService.findUserByEmail(authentication.getName()).orElseThrow();
        commentService.addCommentByMovieId(id, authentication.getName(), comment);
        boolean banIfMore = commentService.countCommentsByUserAndDate(user.getId());
        user.setBanned(banIfMore);
        userService.saveUser(user.getId());
        return "redirect:/film/" + id;
    }
}
