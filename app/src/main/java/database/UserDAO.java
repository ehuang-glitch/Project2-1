package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import database.entites.User;

@Dao
public interface UserDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User...user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + CanvasCloneDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUser();

    @Query("DELETE from " + CanvasCloneDatabase.USER_TABLE) void deleteALL();

    @Query("SELECT * FROM " + CanvasCloneDatabase.USER_TABLE + " WHERE username = :username ")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + CanvasCloneDatabase.USER_TABLE + " WHERE id = :userID")
    LiveData<User> getUserByUserID(int userID);




}

