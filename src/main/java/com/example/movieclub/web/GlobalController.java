package com.example.movieclub.web;

import com.example.movieclub.domain.user.User;
import com.example.movieclub.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    private final UserService userService;

    @ModelAttribute("user")
    public User layout(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return userService.findUserByEmail(authentication.getName()).orElse(null);
        } else {
            return null;
        }
    }
}
