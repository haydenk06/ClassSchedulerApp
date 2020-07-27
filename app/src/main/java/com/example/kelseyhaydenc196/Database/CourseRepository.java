package com.example.kelseyhaydenc196.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.kelseyhaydenc196.Database.DAOs.CourseDao;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Utilities.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseRepository {
    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;
    private CourseDB db;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseRepository(Application application) {
        db = CourseDB.getInstance(application);
        courseDao = db.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public void insert(Course course) {
        new CourseRepository.InsertCourseAsyncTask(courseDao).execute(course);

    }

    public void insertAllCourses(){
        executor.execute(()-> db.courseDao().insertAllCourses(SampleData.getCourses()));
    }

    public void update(Course course) {
        new CourseRepository.UpdateCourseAsyncTask(courseDao).execute(course);

    }

    public void delete(Course course) {
        new CourseRepository.DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }


    public LiveData<List<Course>> getCoursesByTerm(int termId) {
        return courseDao.getCoursesByTerm(termId);
    }

    public LiveData<List<Course>> getCoursesByMentor(int mentorId) {
        return courseDao.getCoursesByMentor(mentorId);
    }

    public LiveData<List<Course>> getCoursesById(int courseId) {
        return courseDao.getCoursesById(courseId);
    }

    public LiveData<List<Course>> getRemoveMentorFromCourse(int mentorId) {
        return courseDao.getCoursesById(mentorId);
    }

    ////////// Async Tasks /////////
    public void deleteAllCourses() {
        new DeleteAllCoursesAsyncTask(courseDao).execute();
    }

    public int getCourseCountByTerm(int termId) {
        return courseDao.getCourseCountByTerm(termId);
    }

    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private InsertCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.insert(courses[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private UpdateCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.update(courses[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private DeleteCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.delete(courses[0]);
            return null;
        }
    }

    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CourseDao courseDao;

        private DeleteAllCoursesAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            courseDao.deleteAllCourses();
            return null;
        }
    }
}
