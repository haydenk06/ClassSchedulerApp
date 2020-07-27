package com.example.kelseyhaydenc196.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.DAOs.AssessmentDao;
import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.Utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentRepository {
    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> allAssessments;
    private AssessmentDB db;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentRepository(Application application) {
        db = AssessmentDB.getInstance(application);
        assessmentDao = db.assessmentDao();
        allAssessments = assessmentDao.getAllAssessments();
    }

    public void insert(Assessment assessment) {
        new AssessmentRepository.InsertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void insertAllAssessments(){
        executor.execute(()-> db.assessmentDao().insertAllAssessments(SampleData.getAssessments()));
    }

    public void update(Assessment assessment) {
        new AssessmentRepository.UpdateAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void delete(Assessment assessment) {
        new AssessmentRepository.DeleteAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void deleteAllAssessments() {
        new AssessmentRepository.DeleteAllAssessmentsAsyncTask(assessmentDao).execute();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(int courseId) {
        return assessmentDao.getAssessmentsByCourse(courseId);
    }

    public LiveData<List<Assessment>> getAssessmentById(int assessmentId) {
        return assessmentDao.getAssessmentById(assessmentId);
    }

    ////////// Async Tasks //////////
    private static class InsertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private InsertAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.insert(assessments[0]);
            return null;
        }
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private UpdateAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.update(assessments[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private DeleteAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.delete(assessments[0]);
            return null;
        }
    }

    private static class DeleteAllAssessmentsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AssessmentDao assessmentDao;

        private DeleteAllAssessmentsAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDao.deleteAllAssessments();
            return null;
        }
    }
}
