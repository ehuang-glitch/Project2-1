package com.example.project2.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2.database.entities.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Course c);

    @Update
    void update(Course c);

    @Delete
    void delete(Course c);

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    Course getCourseById(int id);

    @Query("SELECT * FROM courses ORDER BY name ASC")
    List<Course> getAllCourses();

    @Query("SELECT c.* FROM courses c JOIN course_users cu ON c.id = cu.courseId WHERE cu.userId = :userId ORDER BY c.name ASC")
    List<Course> getCoursesByUser(int userId);

    @Query("SELECT COUNT(*) FROM courses")
    int count();
}
