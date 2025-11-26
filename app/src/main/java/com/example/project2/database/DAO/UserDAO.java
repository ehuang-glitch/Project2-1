
package com.example.project2.database.dao;

import androidx.room.*;
import com.example.project2.database.entities.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(User... u);

    @Query("SELECT * FROM users ORDER BY username ASC")
    List<User> getAll();
}
