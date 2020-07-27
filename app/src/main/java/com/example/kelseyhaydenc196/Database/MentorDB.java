package com.example.kelseyhaydenc196.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kelseyhaydenc196.Database.DAOs.MentorDao;
import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.Utilities.SampleData;

@Database(entities = {Mentor.class, Term.class, Assessment.class, Course.class}, version = 13, exportSchema = false)
abstract class MentorDB extends RoomDatabase {
    private static MentorDB instance;

    public abstract MentorDao mentorDao();

    public static synchronized MentorDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MentorDB.class, "mentor_db").fallbackToDestructiveMigration().addCallback(roomCallback).build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new MentorDB.PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private MentorDao mentorDao;

        private PopulateDbAsyncTask(MentorDB db) {
            mentorDao = db.mentorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mentorDao.insertAllMentors(SampleData.getMentors());
            return null;
        }
    }
}
