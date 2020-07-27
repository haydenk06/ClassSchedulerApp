package com.example.kelseyhaydenc196.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.MentorRepository;
import com.example.kelseyhaydenc196.Model.Mentor;

import java.util.List;

public class MentorViewModel extends AndroidViewModel {
    private MentorRepository repository;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Mentor>> mentorById;

    public MentorViewModel(@NonNull Application application) {
        super(application);
        repository = new MentorRepository(application);
        allMentors = repository.getAllMentors();
    }

    public void insert(Mentor mentor) {
        repository.insert(mentor);
    }

    public void update(Mentor mentor) {
        repository.update(mentor);
    }

    public void delete(Mentor mentor) {
        repository.delete(mentor);
    }

    public void deleteAllMentors() {
        repository.deleteAllMentors();
    }

    public LiveData<List<Mentor>> getAllMentors() {
        return allMentors;
    }

    public LiveData<List<Mentor>> getMentorById(int mentorId) {
        return mentorById = repository.getMentorById(mentorId);
    }

    public void addSampleMentors() {
        repository.insertAllMentors();
    }

}
