package com.example.project2.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2.database.entities.User; // Ensure this import matches your User file location

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + CourseDatabase.USER_TABLE + " ORDER BY username")
    List<User> getAllUsers();

    @Query("DELETE FROM " + CourseDatabase.USER_TABLE)
    void deleteAll();

    // Added for Login functionality: Fetch a user by username to check password
    @Query("SELECT * FROM " + CourseDatabase.USER_TABLE + " WHERE username = :username")
    User getUserByUsername(String username);

    // Added for Dashboard functionality: Fetch the currently logged-in user
    @Query("SELECT * FROM " + CourseDatabase.USER_TABLE + " WHERE id = :id")
    User getUserById(int id);
}