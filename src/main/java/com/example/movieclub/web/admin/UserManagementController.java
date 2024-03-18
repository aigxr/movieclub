package com.example.movieclub.web.admin;

import com.example.movieclub.domain.comment.CommentService;
import com.example.movieclub.domain.rating.RatingService;
import com.example.movieclub.domain.user.User;
import com.example.movieclub.domain.user.UserService;
import com.example.movieclub.domain.user.dto.UserCredentialsDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserManagementController {
    private final UserService userService;
    private final CommentService commentService;
    private final RatingService ratingService;
    @GetMapping("/admin/manage-user")
    public String manageUsers(Model model) {
        List<UserCredentialsDto> allUsers = userService.findAllUsers();
        model.addAttribute("users", allUsers);
        return "admin/manage-user";
    }

    @Transactional
    @PostMapping("/admin/manage-user/{username}")
    public String deleteUser(@PathVariable String username, RedirectAttributes redirectAttributes) {
        User user = userService.findUserByUsername(username).orElseThrow();
        ratingService.deleteRatingByUserId(user.getId());
        commentService.deleteCommentByUserId(user.getId());
        userService.deleteUser(user.getEmail());
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Użytkownik %s został usunięty".formatted(username)
        );
        return "redirect:/admin";
    }

    @Transactional
    @GetMapping("/admin/ban-user/{username}")
    public String banUser(@PathVariable String username, RedirectAttributes redirectAttributes) {
        User user = userService.findUserByUsername(username).orElseThrow();
        user.setBanned(true);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Użytkownik %s został zbanowany pomyślnie".formatted(user.getUsername())
        );
        return "redirect:/admin/manage-user";
    }
    @Transactional
    @GetMapping("/admin/unban-user/{username}")
    public String unbanUser(@PathVariable String username, RedirectAttributes redirectAttributes) {
        User user = userService.findUserByUsername(username).orElseThrow();
        user.setBanned(false);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Użytkownik %s został zbanowany pomyślnie".formatted(user.getUsername())
        );
        return "redirect:/admin/manage-user";
    }
}
