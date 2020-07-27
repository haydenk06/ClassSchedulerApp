package com.example.kelseyhaydenc196.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.TermRepository;
import com.example.kelseyhaydenc196.Model.Term;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
    private TermRepository repository;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Term>> termById;


    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new TermRepository(application);
        allTerms = repository.getAllTerms();
    }

    public void insert(Term term) {
        repository.insert(term);
    }

    public void update(Term term) {
        repository.update(term);
    }

    public void delete(Term term) {
        repository.delete(term);
    }

    public void deleteAllTerms() {
        repository.deleteAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Term>> getTermById(int termId) {
        return termById = repository.getTermById(termId);
    }

    public void addSampleTerms() {
        repository.insertAllTerms();
    }
}
