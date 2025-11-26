package com.example.project2.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2.database.entities.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course... c);

    @Query("SELECT * FROM courses WHERE userId=:u ORDER BY name ASC")
    List<Course> getByUser(int u);

    @Query("SELECT * FROM courses ORDER BY name ASC")
    List<Course> getAllCourses();

    @Query("SELECT COUNT(*) FROM courses")
    int count();
}
