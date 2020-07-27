package com.example.kelseyhaydenc196.Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kelseyhaydenc196.Database.DAOs.CourseDao;
import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.Utilities.SampleData;


@Database(entities = {Course.class, Term.class, Mentor.class, Assessment.class}, version = 22, exportSchema = false)

abstract class CourseDB extends RoomDatabase {
    private static CourseDB instance;

    public abstract CourseDao courseDao();

    public static synchronized CourseDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CourseDB.class, "course_db").fallbackToDestructiveMigration().addCallback(roomCallback).build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new CourseDB.PopulateDBAsyncTask(instance).execute();
        }
    };


    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private CourseDao courseDao;

        private PopulateDBAsyncTask(CourseDB db) {
            courseDao = db.courseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            courseDao.insertAllCourses(SampleData.getCourses());
            return null;
        }
    }

}