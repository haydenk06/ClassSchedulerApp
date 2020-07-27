package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.AssessmentViewModel;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_DUE_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_TITLE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_TYPE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_COURSE_ID;

public class AssessmentDetailActivity extends AppCompatActivity {
    public static final int ADD_ASSESSMENT_REQUEST = 2;
    public static final int ADD_COURSE_REQUEST = 3;

    private AssessmentViewModel assessmentViewModel;

    ///// used for setting  term information /////
    private TextView textViewAssessmentTitle;
    private TextView textViewDueDate;
    private TextView textViewType;
    private TextView textViewCourse;

    private int assessmentId;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        setContentView(R.layout.activity_detail_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        ////////// Finding view items for displaying //////////
        textViewAssessmentTitle = findViewById(R.id.text_view_assessment_title);
        textViewDueDate = findViewById(R.id.text_view_due_date);
        textViewType = findViewById(R.id.text_view_type);
        textViewCourse = findViewById(R.id.text_view_course);

        textViewAssessmentTitle.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TITLE));
        textViewDueDate.setText(intent.getStringExtra(EXTRA_ASSESSMENT_DUE_DATE));
        textViewType.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TYPE));

        courseId = intent.getIntExtra(EXTRA_COURSE_ID, -1);
        assessmentId = intent.getIntExtra(EXTRA_ASSESSMENT_ID, -1);

        ////////// button to open add/edit term window //////////
        FloatingActionButton buttonEditA = findViewById(R.id.fab_edit_assessment);
        buttonEditA.setOnClickListener(v -> {
            Intent intent2 = new Intent(AssessmentDetailActivity.this, AddEditAssessmentActivity.class);
            intent2.putExtra(EXTRA_ASSESSMENT_TITLE, textViewAssessmentTitle.getText().toString());
            intent2.putExtra(EXTRA_ASSESSMENT_DUE_DATE, textViewDueDate.getText());
            intent2.putExtra(EXTRA_ASSESSMENT_TYPE, textViewType.getText());
            intent2.putExtra(EXTRA_ASSESSMENT_ID, assessmentId);
            intent2.putExtra(EXTRA_COURSE_ID, courseId);

            startActivityForResult(intent2, ADD_ASSESSMENT_REQUEST);
        });
        ////////// Course is passed in by ID so I am checking ID and displaying courseTitle in a text view
        CourseViewModel cViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        cViewModel.getCoursesById(courseId).observe(this, courseList -> {
            if (courseList != null) {
                for (Course c : courseList) {
                    String courseTitle = c.getCourseTitle();
                    textViewCourse.setText(courseTitle);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {

            String assessmentTitle = data.getStringExtra(EXTRA_ASSESSMENT_TITLE);
            String dueDate = data.getStringExtra(EXTRA_ASSESSMENT_DUE_DATE);
            String type = data.getStringExtra(EXTRA_ASSESSMENT_TYPE);
            int courseId = data.getIntExtra(EXTRA_COURSE_ID, 0);

            ///// converting string  into dates to store in DB /////
            Date convertedDue = Dates.stringToDateConverter(dueDate);

            Assessment assessment = new Assessment(assessmentTitle, type, convertedDue, courseId);
            assessment.setAssessmentId(assessmentId);
            assessmentViewModel.update(assessment);

            textViewAssessmentTitle.setText(assessmentTitle);
            textViewDueDate.setText(dueDate);
            textViewType.setText(type);

            ////////// Course is passed in by ID so I am checking ID and displaying courseTitle in a text view
            CourseViewModel cViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
            cViewModel.getCoursesById(courseId).observe(this, courseList -> {
                if (courseList != null) {
                    for (Course c : courseList) {
                        String courseTitle = c.getCourseTitle();
                        textViewCourse.setText(courseTitle);
                    }
                }
            });

            Toast.makeText(this, "Assessment Updated", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Assessment not updated", Toast.LENGTH_SHORT).show();
    }

}
