package com.assessment.controller;

import com.assessment.model.User;
import com.assessment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;

/**
 * User Controller
 * Handles operations related to user management, including fetching user data and updating user profiles.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;

    /**
     * Update User Profile
     * 
     * - A user can update only their own profile.
     * - An admin can update any user's profile.
     *
     * @param id The ID of the user to update.
     * @param updatedUser The updated user data.
     * @param authentication The authentication object containing the logged-in user's details.
     * @return A success response with the updated user data or an error message.
     */
    @PutMapping("/user/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedUser, Authentication authentication) {
        String loggedInUserEmail = authentication.getName(); // Get logged-in user's email
    
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    
        // Allow update if the user is an admin or updating their own data
        if (isAdmin || user.getEmail().equals(loggedInUserEmail)) {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "User updated successfully",
                "user", userRepository.save(user)
            ));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "status", "error",
                "message", "You are not allowed to update this user."
            ));
        }
    }

    /**
     * Get User(s) Data
     * 
     * - If an ID is provided, fetches a specific user's data.
     * - If no ID is provided, fetches all users with a total count.
     * - Only admins are allowed to access this endpoint.
     *
     * @param id (Optional) The ID of the user to fetch.
     * @param authentication The authentication object containing the logged-in user's details.
     * @return A list of users or a single user's data based on the request parameters.
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String id, Authentication authentication) {
        // Ensure user has admin privileges
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "status", "error",
                "message", "You are not permitted to access this data"
            ));
        }

        // Fetch a specific user by ID
        if (id != null) {
            return userRepository.findById(id)
                    .map(user -> ResponseEntity.ok(Map.of(
                            "status", "success",
                            "message", "Fetched user successfully",
                            "user", user
                    )))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "status", "error",
                            "message", "User not found"
                    )));
        }

        // Fetch all users
        List<User> users = userRepository.findAll();
        int totalUsers = users.size(); // Get total user count

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "status", "success",
                    "message", "No users found",
                    "totalUsers", 0
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Fetched users successfully",
                "totalUsers", totalUsers,
                "users", users
        ));
    }
}
