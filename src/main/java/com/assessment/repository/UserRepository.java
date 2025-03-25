package com.assessment.repository;

import com.assessment.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * User Repository
 * Provides database access methods for User-related operations.
 * Extends MongoRepository to interact with MongoDB.
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find a user by email.
     * 
     * @param email The email address of the user.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by ID.
     * 
     * @param id The unique identifier of the user.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findById(String id);
}
