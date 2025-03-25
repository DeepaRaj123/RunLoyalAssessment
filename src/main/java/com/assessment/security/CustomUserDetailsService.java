package com.assessment.security;

import com.assessment.model.User;
import com.assessment.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom User Details Service
 * Implements Spring Security's UserDetailsService to fetch user details from the database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor to inject UserRepository dependency.
     * 
     * @param userRepository The repository used to fetch user details.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user details by email (used for authentication).
     * 
     * @param email The email address of the user.
     * @return UserDetails object containing authentication details.
     * @throws UsernameNotFoundException if no user is found with the given email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // Password should be stored in hashed format (BCrypt)
                .roles(user.getRole()) // Set roles (ADMIN/USER)
                .build();
    }
}
