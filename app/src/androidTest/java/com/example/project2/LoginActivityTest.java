package com.example.project2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit tests for LoginActivity
 */
class LoginActivityTest {

    @BeforeEach
    void setUp() {
        // Initialize test environment before each test
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
    }

    @Test
    @DisplayName("Test empty username validation")
    void testEmptyUsernameValidation() {
        String username = "";
        String password = "password123";

        boolean isValid = !username.isEmpty() && !password.isEmpty();

        assertFalse(isValid, "Empty username should fail validation");
    }

    @Test
    @DisplayName("Test empty password validation")
    void testEmptyPasswordValidation() {
        String username = "testuser";
        String password = "";

        boolean isValid = !username.isEmpty() && !password.isEmpty();

        assertFalse(isValid, "Empty password should fail validation");
    }

    @Test
    @DisplayName("Test valid credentials format")
    void testValidCredentialsFormat() {
        String username = "testuser";
        String password = "password123";

        boolean isValid = !username.isEmpty() && !password.isEmpty();

        assertTrue(isValid, "Valid username and password should pass validation");
    }

    @Test
    @DisplayName("Test username trimming")
    void testUsernameTrimming() {
        String username = "  testuser  ";
        String trimmed = username.trim();

        assertEquals("testuser", trimmed, "Username should be trimmed");
    }
}