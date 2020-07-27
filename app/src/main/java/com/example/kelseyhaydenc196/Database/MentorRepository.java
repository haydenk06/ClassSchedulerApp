package com.example.kelseyhaydenc196.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.DAOs.MentorDao;
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.Utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorRepository {
    private MentorDao mentorDao;
    private LiveData<List<Mentor>> allMentors;
    private MentorDB db;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MentorRepository(Application application) {
        db = MentorDB.getInstance(application);
        mentorDao = db.mentorDao();
        allMentors = mentorDao.getAllMentors();
    }

    public void insert(Mentor mentor) {
        new MentorRepository.InsertMentorAsyncTask(mentorDao).execute(mentor);

    }

    public void insertAllMentors(){
        executor.execute(()-> db.mentorDao().insertAllMentors(SampleData.getMentors()));
    }

    public void update(Mentor mentor) {
        new MentorRepository.UpdateMentorAsyncTask(mentorDao).execute(mentor);

    }

    public void delete(Mentor mentor) {
        new MentorRepository.DeleteMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void deleteAllMentors() {
        new MentorRepository.DeleteAllMentorsAsyncTask(mentorDao).execute();
    }

    public LiveData<List<Mentor>> getAllMentors() {
        return allMentors;
    }

    public LiveData<List<Mentor>> getMentorById(int mentorId) {
        return mentorDao.getMentorById(mentorId);
    }

    ////////// Async Tasks /////////
    private static class InsertMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private InsertMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.insert(mentors[0]);
            return null;
        }
    }

    private static class UpdateMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private UpdateMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.update(mentors[0]);
            return null;
        }
    }

    private static class DeleteMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private DeleteMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.delete(mentors[0]);
            return null;
        }
    }

    private static class DeleteAllMentorsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MentorDao mentorDao;

        private DeleteAllMentorsAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mentorDao.deleteAllMentors();
            return null;
        }
    }
}
