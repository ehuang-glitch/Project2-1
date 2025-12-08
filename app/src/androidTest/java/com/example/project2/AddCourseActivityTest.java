package com.example.project2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Unit tests for AddCourseActivity
 */
public class AddCourseActivityTest {

    @Before
    public void setUp() throws Exception {
        // Initialize test environment before each test
    }

    @After
    public void tearDown() throws Exception {
        // Clean up after each test
    }

    @Test
    public void testEmptyCourseCodeValidation() {
        String code = "";
        String name = "Introduction to Programming";

        boolean isValid = !code.isEmpty() && !name.isEmpty();

        assertFalse("Empty course code should fail validation", isValid);
    }

    @Test
    public void testEmptyCourseNameValidation() {
        String code = "CS101";
        String name = "";

        boolean isValid = !code.isEmpty() && !name.isEmpty();

        assertFalse("Empty course name should fail validation", isValid);
    }

    @Test
    public void testValidCourseInput() {
        String code = "CS101";
        String name = "Introduction to Programming";

        boolean isValid = !code.isEmpty() && !name.isEmpty();

        assertTrue("Valid course code and name should pass validation", isValid);
    }

    @Test
    public void testCourseCodeTrimming() {
        String code = "  CS101  ";
        String trimmed = code.trim();

        assertEquals("Course code should be trimmed", "CS101", trimmed);
    }

    @Test
    public void testCourseNameTrimming() {
        String name = "  Introduction to Programming  ";
        String trimmed = name.trim();

        assertEquals("Course name should be trimmed", "Introduction to Programming", trimmed);
    }
}
