package com.example.kelseyhaydenc196.Database.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.kelseyhaydenc196.Model.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCourses(List<Course> courses);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("DELETE FROM course_table")
    void deleteAllCourses();

    @Query("SELECT * FROM course_table ORDER BY endDate DESC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM course_table ORDER BY startDate ASC")
    List<Course> getCourseList();

    @Query("SELECT * FROM course_table WHERE termId = :termId")
    LiveData<List<Course>> getCoursesByTerm(final int termId);

    @Query("SELECT * FROM course_table WHERE mentorId = :mentorId")
    LiveData<List<Course>> getCoursesByMentor(final int mentorId);

    @Query("SELECT * FROM course_table WHERE id = :courseId")
    LiveData<List<Course>> getCoursesById(final int courseId);

    @Query("SELECT COUNT(*) FROM course_table WHERE id = :courseId")
    int getCourseCountByTerm(final int courseId);

}
