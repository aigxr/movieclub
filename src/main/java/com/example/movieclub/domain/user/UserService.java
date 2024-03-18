package com.example.movieclub.domain.user;

import com.example.movieclub.config.security.CustomUserDetailsService;
import com.example.movieclub.domain.comment.CommentRepository;
import com.example.movieclub.domain.comment.CommentService;
import com.example.movieclub.domain.rating.RatingRepository;
import com.example.movieclub.domain.storage.FileStorageService;
import com.example.movieclub.domain.user.dto.UserCredentialsDto;
import com.example.movieclub.domain.user.dto.UserCredentialsDtoMapper;
import com.example.movieclub.domain.user.dto.UserCredentialsSaveDto;
import com.example.movieclub.domain.user.dto.UserRegistrationDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserService {
    private static final String DEFAULT_USER_ROLE = "USER";
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findUserById(Long id) {
        return userRepository.findUserById(id);
    }
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
    public List<UserCredentialsDto> findUsersByKeyword(String keyword) {
        List<User> users;
        if (keyword == null) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findUserByUsernameContainingIgnoreCase(keyword);
        }
        return users.stream().map(UserCredentialsDtoMapper::map).toList();
    }
    public List<UserCredentialsDto> findAllUsers() {
        return userRepository.findAll().stream().map(UserCredentialsDtoMapper::map).toList();
    }
    @Transactional
    public void registerUserWithDefaultRole(UserRegistrationDto userRegistration) {
        UserRole defaultRole = userRoleRepository.findByName(DEFAULT_USER_ROLE).orElseThrow();
        User user = new User();
        user.setEmail(userRegistration.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        if (userRegistration.getUsername().isEmpty()) {
            Random random = new Random();
            int i = random.nextInt(10, 100000); //TODO dodac tworzenie unikalnych cyfr
            user.setUsername("user" + i);
        } else {
            user.setUsername(userRegistration.getUsername());
        }
        user.getRoles().add(defaultRole);
        userRepository.save(user);
    }

    @Transactional
    public void changeUsername(UserCredentialsDto userDto, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        user.setUsername(userDto.getUsername());
        userRepository.save(user);
    }

    @Transactional
    public void changeEmail(String username, String newUsername) {
        User user = userRepository.findByEmail(username).orElseThrow();
        user.setEmail(newUsername);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(newPassword); // NIE KRYPTUJEMY W TYM MIEJSCU HASLA, JEST JUZ ONO ZROBIONE W SERVICE
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        commentRepository.deleteCommentByUserId(user.getId());
        ratingRepository.deleteRatingByUserId(user.getId()).orElseThrow();
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public void saveImage(MultipartFile profilePicture, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        String savedFileName = fileStorageService.saveImage(profilePicture);
        user.setProfilePicture(savedFileName);
        userRepository.save(user);
    }

    public void saveUser(Long id) {
        User user = findUserById(id).orElseThrow();
        userRepository.save(user);
    }
}
