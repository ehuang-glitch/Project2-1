package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.project2.database.CourseDatabase;

@Entity(tableName = CourseDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }
    // ... Add getters and setters (copy from reference)
}