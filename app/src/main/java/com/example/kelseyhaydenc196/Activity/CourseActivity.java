package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.CourseAdapter;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_END_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_MENTOR_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NOTE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_START_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_STATUS;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TITLE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_ID;

public class CourseActivity extends AppCompatActivity {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int OPEN_DETAIL_REQUEST = 2;

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ///// button to add a new course /////
        FloatingActionButton buttonAddCourse = findViewById(R.id.button_add_course);
        buttonAddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(CourseActivity.this, AddCourseActivity.class);
            startActivityForResult(intent, ADD_COURSE_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, adapter::setCourseList);

        /////////// swipe to delete selected course ///////////
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            ///// drag and drop /////
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            ///// swiping to delete /////
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                courseViewModel.delete(adapter.getCourseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(CourseActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        ///// used for clicking on item and loading detail screen /////
        adapter.setOnItemClickListener(course -> {
            ///// loading data to course details screen /////
            Intent intent = new Intent(CourseActivity.this, CourseDetailActivity.class);
            intent.putExtra(EXTRA_ID, course.getId());
            intent.putExtra(EXTRA_TITLE, course.getCourseTitle());
            intent.putExtra(EXTRA_START_DATE, Dates.dateToString(course.getStartDate()));
            intent.putExtra(EXTRA_END_DATE, Dates.dateToString(course.getEndDate()));
            intent.putExtra(EXTRA_STATUS, course.getStatus());
            intent.putExtra(EXTRA_NOTE, course.getNote());
            intent.putExtra(EXTRA_TERM_ID, course.getTermId());
            intent.putExtra(EXTRA_MENTOR_ID, course.getMentorId());
            intent.putExtra(EXTRA_ASSESSMENT_ID, -1);
            startActivityForResult(intent, OPEN_DETAIL_REQUEST);
        });
    }

    ///// saving from add course screen /////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(EXTRA_TITLE);
            String startDate = data.getStringExtra(EXTRA_START_DATE);
            String endDate = data.getStringExtra(EXTRA_END_DATE);
            String status = data.getStringExtra(EXTRA_STATUS);
            String note = data.getStringExtra(EXTRA_NOTE);
            int termId = data.getIntExtra(EXTRA_TERM_ID, -1);
            int mentorId = data.getIntExtra(EXTRA_MENTOR_ID, -1);
            //int id = data.getIntExtra(AddCourseActivity.EXTRA_ID, -1);

            ///// converting string into dates to store in DB /////
            Date convertedStart = Dates.stringToDateConverter(startDate);
            Date convertedEnd = Dates.stringToDateConverter(endDate);

            Course course = new Course(title, convertedStart, convertedEnd, status, note, termId, mentorId);
            courseViewModel.insert(course);

            Toast.makeText(this, "New Course Saved", Toast.LENGTH_SHORT).show();
        }
    }
}