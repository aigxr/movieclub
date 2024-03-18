package com.example.movieclub.config.security;

import com.example.movieclub.domain.user.UserService;
import com.example.movieclub.domain.user.dto.UserCredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findCredentialsByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

    private UserDetails createUserDetails(UserCredentialsDto credentials) {
        return User.builder()
                .username(credentials.getEmail())
                .password(credentials.getPassword())
                .roles(credentials.getRoles().toArray(String[]::new))
                .build();
    }

    public void changeUsername(String currentUsername, String newUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(currentUsername)) {
            UserDetails userDetails = loadUserByUsername(currentUsername);
            userService.changeEmail(authentication.getName(), newUsername);
            UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                    userDetails, authentication.getCredentials(), userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        }
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(username)) {
            UserDetails userDetails = loadUserByUsername(username);
            if (passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
                String encodedPassword = passwordEncoder.encode(newPassword);

                userService.changePassword(authentication.getName(), encodedPassword);
                UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                        userDetails, newPassword, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            } else {
                throw new RuntimeException("Passwords incorrect");
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
