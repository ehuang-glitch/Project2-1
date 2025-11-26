package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.project2.database.CourseDatabase;

@Entity(tableName = CourseDatabase.COURSE_TABLE)
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String courseCode;
    private String courseName;
    private String grade; // e.g. "95%"
    private int userId; // Foreign Key to link course to a user

    public Course(String courseCode, String courseName, String grade, int userId) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.grade = grade;
        this.userId = userId;
    }
    // ... Add getters and setters
}