package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import database.entites.CanvasClone;

@Dao
public interface CanvasCloneDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CanvasClone gymlog);
    @Query("SELECT * FROM " + CanvasCloneDatabase.GYM_LOG_TABLE + " ORDER BY date DESC")
    List<CanvasClone> getAllRecords();


    @Query("SELECT * FROM " + CanvasCloneDatabase.GYM_LOG_TABLE +" WHERE userID = :userID ORDER BY date DESC")
    LiveData<List<CanvasClone>> getAllLogsByUserID(int userID);

    @Query("SELECT * FROM " + CanvasCloneDatabase.GYM_LOG_TABLE +" WHERE userID = :loggedInUserID ORDER BY date DESC")
    List<CanvasClone> getAllRecordsByUserID(int loggedInUserID);

    @Query("SELECT * FROM " + CanvasCloneDatabase.GYM_LOG_TABLE +" WHERE userID = :loggedInUserID ORDER BY date DESC")
    LiveData<List<CanvasClone>> getAllRecordsByUserIDLiveData(int loggedInUserID);
}
