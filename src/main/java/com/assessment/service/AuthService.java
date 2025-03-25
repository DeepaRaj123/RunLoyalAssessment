package com.assessment.service;

import com.assessment.model.User;
import com.assessment.repository.UserRepository;
import com.assessment.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

/**
 * Authentication Service
 * Handles user registration, authentication (login), and user retrieval by ID.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registers a new user in the system.
     * 
     * - Checks if the email is already registered.
     * - Hashes the password before storing it.
     * - Generates a JWT token upon successful registration.
     *
     * @param user The user details for registration.
     * @return ResponseEntity with success or error message.
     */
    public ResponseEntity<?> registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Email already exists"
            ));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password before saving
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        // Construct success response
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "User registered successfully",
            "token", token,
            "userId", user.getId(),
            "email", user.getEmail()
        ));
    }

    /**
     * Authenticates a user based on email and password.
     * 
     * - Checks if the user exists.
     * - Validates password using BCrypt.
     * - Generates a JWT token upon successful login.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return ResponseEntity with success or error message.
     */
    public ResponseEntity<?> login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", "error",
                "message", "User not found"
            ));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Invalid credentials"
            ));
        }

        String token = jwtUtil.generateToken(email);

        // Construct success response
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "User logged in successfully",
            "token", token,
            "userId", user.getId(),
            "email", user.getEmail()
        ));
    }

    /**
     * Fetches a user by ID.
     * 
     * - Throws an exception if the user is not found.
     *
     * @param id The unique ID of the user.
     * @return The User object if found.
     */
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
