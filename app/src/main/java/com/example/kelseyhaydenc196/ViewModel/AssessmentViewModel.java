package com.example.kelseyhaydenc196.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.AssessmentRepository;
import com.example.kelseyhaydenc196.Model.Assessment;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allAssessments;
    private LiveData<List<Assessment>> allAssessmentsById;
    private LiveData<List<Assessment>> allAssessmentsByCourse;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }

    public void update(Assessment assessment) {
        repository.update(assessment);
    }

    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }

    public void deleteAllAssessments() {
        repository.deleteAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(int courseId) {
        return allAssessmentsByCourse = repository.getAssessmentsByCourse(courseId);
    }

    public void addSampleAssessments() {
        repository.insertAllAssessments();
    }

}
