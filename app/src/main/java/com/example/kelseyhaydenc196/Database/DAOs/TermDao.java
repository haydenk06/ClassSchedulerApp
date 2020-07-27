package com.example.kelseyhaydenc196.Database.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kelseyhaydenc196.Model.Term;

import java.util.List;


@Dao
public interface TermDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Term term);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTerms(List<Term> terms);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("DELETE FROM term_table")
    void deleteAllTerms();

    @Query("SELECT * FROM term_table ORDER BY termTitle ASC")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM term_table WHERE termId = :termId")
    LiveData<List<Term>> getTermById(final int termId);

}
