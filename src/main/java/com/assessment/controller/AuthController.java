package com.assessment.controller;

import com.assessment.model.User;
import com.assessment.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles user authentication-related operations such as signup and signin.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    /**
     * User Registration (Signup)
     * 
     * @param user The user details (first name, last name, email, password, etc.)
     * @return A response entity with a success status and an authentication token upon successful registration
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        return authService.registerUser(user);
    }

    /**
     * User Authentication (Signin)
     * 
     * @param user The user login credentials (email and password)
     * @return A response entity with a success status and an authentication token if credentials are valid
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody User user) {
        return authService.login(user.getEmail(), user.getPassword());
    }
}
