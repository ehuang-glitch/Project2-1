package com.example.project2.database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2.Course; // Ensure this points to where you created Course.java

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    // Selects all courses belonging to a specific user
    @Query("SELECT * FROM " + CourseDatabase.COURSE_TABLE + " WHERE userId = :userId ORDER BY courseName ASC")
    List<Course> getCoursesByUserId(int userId);

    // Optional: Select all courses regardless of user (good for debugging)
    @Query("SELECT * FROM " + CourseDatabase.COURSE_TABLE)
    List<Course> getAllCourses();
}