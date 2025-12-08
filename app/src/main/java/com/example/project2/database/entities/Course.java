package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String code;
    private String name;

    public Course() {}

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String c) { this.code = c; }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
}
