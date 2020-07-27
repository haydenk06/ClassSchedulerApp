package com.example.kelseyhaydenc196.Database.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kelseyhaydenc196.Model.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Assessment assessment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAssessments(List<Assessment> assessments);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    void deleteAllAssessments();


    @Query("SELECT * FROM assessment_table ORDER BY assessmentId ASC")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE courseId=:courseId")
    LiveData<List<Assessment>> getAssessmentsByCourse(int courseId);

    @Query("SELECT * FROM assessment_table WHERE assessmentId = :assessmentId")
    LiveData<List<Assessment>> getAssessmentById(final int assessmentId);
}
