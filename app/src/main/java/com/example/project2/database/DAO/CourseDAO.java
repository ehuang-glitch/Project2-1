
package com.example.project2.database.dao;

import androidx.room.*;
import com.example.project2.database.entities.Course;
import java.util.List;

@Dao
public interface CourseDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Course... c);

    @Query("SELECT * FROM courses WHERE userId=:u ORDER BY name ASC")
    List<Course> getByUser(int u);

    @Query("SELECT COUNT(*) FROM courses")
    int count();
}
