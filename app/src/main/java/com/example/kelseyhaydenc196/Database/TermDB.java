package com.example.kelseyhaydenc196.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kelseyhaydenc196.Database.DAOs.TermDao;
import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.Utilities.SampleData;

@Database(entities = {Term.class, Assessment.class, Course.class, Mentor.class}, version = 15, exportSchema = false)
abstract class TermDB extends RoomDatabase {
    private static TermDB instance;

    public abstract TermDao termDao();

    public static synchronized TermDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TermDB.class, "term_db").fallbackToDestructiveMigration().addCallback(roomCallback).build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new TermDB.PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;

        private PopulateDbAsyncTask(TermDB db) {
            termDao = db.termDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.insertAllTerms(SampleData.getTerms());
            return null;
        }
    }
}
