package com.example.kelseyhaydenc196.Database.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kelseyhaydenc196.Model.Mentor;

import java.util.List;

@Dao
public interface MentorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Mentor mentor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMentors(List<Mentor> mentors);

    @Update
    void update(Mentor mentor);

    @Delete
    void delete(Mentor mentor);

    @Query("DELETE FROM mentor_table")
    void deleteAllMentors();

    @Query("SELECT * FROM mentor_table ORDER BY name DESC")
    LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM mentor_table WHERE mentorId = :mentorId")
    LiveData<List<Mentor>> getMentorById(final int mentorId);
}
