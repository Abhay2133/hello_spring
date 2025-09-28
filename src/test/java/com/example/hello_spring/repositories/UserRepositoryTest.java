package com.example.hello_spring.repositories;

import com.example.hello_spring.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify JPA/Hibernate configuration with H2 database
 */
@DataJpaTest
@TestPropertySource(properties = "spring.profiles.active=test")
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testSaveAndFindUser() {
        // Create a new user
        User user = new User("testuser", "test@example.com");
        
        // Save the user
        User savedUser = userRepository.save(user);
        
        // Verify the user was saved
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertNotNull(savedUser.getCreatedAt());
    }
    
    @Test
    void testFindByUsername() {
        // Create and save a user
        User user = new User("john", "john@example.com");
        userRepository.save(user);
        
        // Find by username
        Optional<User> foundUser = userRepository.findByUsername("john");
        
        // Verify the user was found
        assertTrue(foundUser.isPresent());
        assertEquals("john", foundUser.get().getUsername());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }
    
    @Test
    void testExistsByUsername() {
        // Create and save a user
        User user = new User("jane", "jane@example.com");
        userRepository.save(user);
        
        // Check if user exists
        assertTrue(userRepository.existsByUsername("jane"));
        assertFalse(userRepository.existsByUsername("nonexistent"));
    }
    
    @Test
    void testFindByEmail() {
        // Create and save a user
        User user = new User("alice", "alice@example.com");
        userRepository.save(user);
        
        // Find by email
        Optional<User> foundUser = userRepository.findByEmail("alice@example.com");
        
        // Verify the user was found
        assertTrue(foundUser.isPresent());
        assertEquals("alice", foundUser.get().getUsername());
    }
}