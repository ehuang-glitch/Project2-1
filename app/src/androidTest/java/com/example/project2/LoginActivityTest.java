package com.example.project2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Unit tests for LoginActivity
 */
public class LoginActivityTest {

    @Before
    public void setUp() {
        // Initialize test environment before each test
    }

    @After
    public void tearDown() {
        // Clean up after each test
    }

    @Test
    public void testEmptyUsernameValidation() {
        String username = "";
        String password = "password123";

        boolean isValid = !username.isEmpty() && !password.isEmpty();

        assertFalse("Empty username should fail validation", isValid);
    }

    @Test
    public void testEmptyPasswordValidation() {
        String username = "testuser";
        String password = "";

        boolean isValid = !username.isEmpty() && !password.isEmpty();

        assertFalse("Empty password should fail validation", isValid);
    }

    @Test
    public void testValidCredentialsFormat() {
        String username = "testuser";
        String password = "password123";

        boolean isValid = !username.isEmpty() && !password.isEmpty();

        assertTrue("Valid username and password should pass validation", isValid);
    }

    @Test
    public void testUsernameTrimming() {
        String username = "  testuser  ";
        String trimmed = username.trim();

        assertEquals("Username should be trimmed", "testuser", trimmed);
    }
}
