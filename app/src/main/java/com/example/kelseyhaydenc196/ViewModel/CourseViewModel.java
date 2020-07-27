package com.example.kelseyhaydenc196.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.CourseRepository;
import com.example.kelseyhaydenc196.Model.Course;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private CourseRepository repository;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> allCoursesByTerm;
    private LiveData<List<Course>> allCoursesByMentor;
    private LiveData<List<Course>> allCoursesById;


    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        allCourses = repository.getAllCourses();
    }

    public void insert(Course course) {
        repository.insert(course);
    }

    public void update(Course course) {
        repository.update(course);
    }


    public void delete(Course course) {
        repository.delete(course);
    }

    public void deleteAllCourses() {
        repository.deleteAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }


    public LiveData<List<Course>> getCoursesByTerm(int termId) {
        return allCoursesByTerm = repository.getCoursesByTerm(termId);
    }

    public LiveData<List<Course>> getCoursesByMentor(int mentorId) {
        return allCoursesByMentor = repository.getCoursesByMentor(mentorId);
    }

    public LiveData<List<Course>> getCoursesById(int courseId) {
        return allCoursesById = repository.getCoursesById(courseId);
    }

    public int getCourseCountByTerm(int termId) {
        return repository.getCourseCountByTerm(termId);
    }

    public void addSampleCourses() {
        repository.insertAllCourses();
    }
}