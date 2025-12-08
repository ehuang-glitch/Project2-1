package com.example.project2.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2.database.entities.Assignment;

import java.util.List;

@Dao
public interface AssignmentDao {

    @Insert
    long insert(Assignment assignment);

    @Update
    void update(Assignment assignment);

    @Delete
    void delete(Assignment assignment);

    @Query("SELECT * FROM assignments WHERE courseId = :courseId ORDER BY dueDate ASC")
    List<Assignment> getAssignmentsByCourse(int courseId);

    @Query("SELECT * FROM assignments WHERE id = :id LIMIT 1")
    Assignment getAssignmentById(int id);

    @Query("SELECT COUNT(*) FROM assignments WHERE courseId = :courseId")
    int countAssignments(int courseId);
}
