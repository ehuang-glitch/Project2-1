package com.example.project2.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project2.database.entities.CourseUser;
import com.example.project2.database.entities.User;

import java.util.List;

@Dao
public interface CourseUserDao {

    @Insert
    void insert(CourseUser... cu);

    @Delete
    void delete(CourseUser cu);

    @Query("SELECT u.* FROM users u JOIN course_users cu ON u.id = cu.userId WHERE cu.courseId = :courseId")
    List<User> getUsersInCourse(int courseId);

    @Query("SELECT * FROM course_users WHERE courseId = :courseId AND userId = :userId LIMIT 1")
    CourseUser getCourseUser(int courseId, int userId);

    @Query("SELECT COUNT(*) FROM course_users WHERE courseId = :courseId AND userId = :userId")
    int isUserEnrolled(int courseId, int userId);

    @Query("DELETE FROM course_users WHERE courseId = :courseId")
    void deleteAllUsersFromCourse(int courseId);
}
