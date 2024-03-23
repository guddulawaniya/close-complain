package com.example.complaintclose.Roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item_table")
    LiveData<List<notes>> getAlldata();

//    @Query("SELECT * FROM item_table WHERE title LIKE '%' || :searchQuery || '%'")
//    LiveData<List<notes>> searchNotes(String searchQuery);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(notes note);

//    @Query("UPDATE item_table set itemname = :titleText,descripation=:discri,date=:date, time=:time where id=:id")
//    void update(String titleText,String discri, String time,String date,int id);

    @Delete
    void delete(notes note);


    @Query("DELETE FROM item_table")
    void deleteAllUsers();

}
