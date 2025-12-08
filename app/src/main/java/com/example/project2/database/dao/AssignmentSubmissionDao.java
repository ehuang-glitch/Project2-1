package com.example.project2.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2.database.entities.AssignmentSubmission;

import java.util.List;

@Dao
public interface AssignmentSubmissionDao {

    @Insert
    void insert(AssignmentSubmission... submission);

    @Update
    void update(AssignmentSubmission submission);

    @Delete
    void delete(AssignmentSubmission submission);

    @Query("SELECT * FROM assignment_submissions WHERE assignmentId = :assignmentId ORDER BY submitted DESC")
    List<AssignmentSubmission> getSubmissionsByAssignment(int assignmentId);

    @Query("SELECT * FROM assignment_submissions WHERE assignmentId = :assignmentId AND userId = :userId LIMIT 1")
    AssignmentSubmission getSubmission(int assignmentId, int userId);

    @Query("SELECT * FROM assignment_submissions WHERE userId = :userId")
    List<AssignmentSubmission> getSubmissionsByUser(int userId);

    @Query("SELECT COUNT(*) FROM assignment_submissions WHERE assignmentId = :assignmentId AND submitted = 1")
    int countSubmitted(int assignmentId);

    @Query("SELECT COUNT(*) FROM assignment_submissions WHERE assignmentId = :assignmentId")
    int countTotal(int assignmentId);
}
