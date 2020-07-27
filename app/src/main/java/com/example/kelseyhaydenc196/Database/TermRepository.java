package com.example.kelseyhaydenc196.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.DAOs.TermDao;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.Utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermRepository {
    private TermDao termDao;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Term>> termById;
    private int termId;
    private TermDB db;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermRepository(Application application) {
        db = TermDB.getInstance(application);
        termDao = db.termDao();
        allTerms = termDao.getAllTerms();
    }

    public void insert(Term term) {
        new TermRepository.InsertTermAsyncTask(termDao).execute(term);

    }

    public void insertAllTerms(){
        executor.execute(()-> db.termDao().insertAllTerms(SampleData.getTerms()));
    }

    public void update(Term term) {
        new TermRepository.UpdateTermAsyncTask(termDao).execute(term);

    }

    ///////// Async Tasks //////////
    public void delete(Term term) {
        new TermRepository.DeleteTermAsyncTask(termDao).execute(term);
    }

    public void deleteAllTerms() {
        new TermRepository.DeleteAllTermsAsyncTask(termDao).execute();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Term>> getTermById(int termId) {
        return termDao.getTermById(termId);
    }

    private static class InsertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private InsertTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.insert(terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private UpdateTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.update(terms[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private DeleteTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.delete(terms[0]);
            return null;
        }
    }

    private static class DeleteAllTermsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;

        private DeleteAllTermsAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAllTerms();
            return null;
        }
    }
}
