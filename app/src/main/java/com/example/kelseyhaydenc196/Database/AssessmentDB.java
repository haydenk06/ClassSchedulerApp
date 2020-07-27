package com.example.kelseyhaydenc196.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kelseyhaydenc196.Database.DAOs.AssessmentDao;
import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.Utilities.SampleData;


@Database(entities = {Assessment.class, Course.class, Term.class, Mentor.class}, version = 14, exportSchema = false)
abstract class AssessmentDB extends RoomDatabase {
    private static AssessmentDB instance;

    public abstract AssessmentDao assessmentDao();

    public static synchronized AssessmentDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AssessmentDB.class, "assessment_db").fallbackToDestructiveMigration().addCallback(roomCallback).build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AssessmentDB.PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private AssessmentDao assessmentDao;

        private PopulateDbAsyncTask(AssessmentDB db) {
            assessmentDao = db.assessmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDao.insertAllAssessments(SampleData.getAssessments());
            return null;
        }
    }
}
