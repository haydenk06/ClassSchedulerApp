package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.AssessmentAdapter;
import com.example.kelseyhaydenc196.MentorAdapter;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.AssessmentViewModel;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;
import com.example.kelseyhaydenc196.ViewModel.MentorViewModel;
import com.example.kelseyhaydenc196.ViewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_DUE_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_TITLE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_TYPE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_COURSE_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_EMAIL;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_END_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_MENTOR_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NAME;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NOTE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_PHONE_NUMBER;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_START_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_STATUS;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TITLE;


public class CourseDetailActivity extends AppCompatActivity{
    public static final int EDIT_COURSE_REQUEST = 2;
    public static final int OPEN_ASSESSMENT_DETAIL = 4;
    public static final int ADD_MENTOR_REQUEST = 3;

    private CourseViewModel courseViewModel;

    private TextView textViewTitle;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private TextView textViewStatus;
    private TextView textViewNote;
    private TextView textViewTerm;

    private int id;
    private int termId;
    private int mentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        setContentView(R.layout.activity_detail_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        ////////// Finding view items for displaying //////////
        textViewTitle = findViewById(R.id.text_view_title);
        textViewStartDate = findViewById(R.id.text_view_start_date);
        textViewEndDate = findViewById(R.id.text_view_end_date);
        textViewStatus = findViewById(R.id.text_view_status);
        textViewNote = findViewById(R.id.text_view_note);
        textViewTerm = findViewById(R.id.text_view_term);

        ///// Setting Title, start + end dates, status and notes /////
        textViewTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        textViewStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
        textViewEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
        textViewStatus.setText(intent.getStringExtra(EXTRA_STATUS));
        textViewNote.setText(intent.getStringExtra(EXTRA_NOTE));

        id = intent.getIntExtra(EXTRA_ID, -1);
        termId = intent.getIntExtra(EXTRA_TERM_ID, -1);
        mentorId = intent.getIntExtra(EXTRA_MENTOR_ID, -1);

        ////////// button to open edit course window //////////
        FloatingActionButton buttonEditCourse = findViewById(R.id.fab_edit_course);
        buttonEditCourse.setOnClickListener(v -> {
            Intent intent2 = new Intent(CourseDetailActivity.this, EditCourseActivity.class);
            intent2.putExtra(EXTRA_TITLE, textViewTitle.getText().toString());
            intent2.putExtra(EXTRA_START_DATE, textViewStartDate.getText());
            intent2.putExtra(EXTRA_END_DATE, textViewEndDate.getText());
            intent2.putExtra(EXTRA_STATUS, textViewStatus.getText());
            intent2.putExtra(EXTRA_NOTE, textViewNote.getText());
            intent2.putExtra(EXTRA_ID, id);
            intent2.putExtra(EXTRA_TERM_ID, termId);
            intent2.putExtra(EXTRA_MENTOR_ID, mentorId);
            startActivityForResult(intent2, EDIT_COURSE_REQUEST);
        });

        //////////// button to open share notes window //////////
        FloatingActionButton buttonShare = findViewById(R.id.fab_share);
        buttonShare.setOnClickListener(v -> {
            Intent intent3 = new Intent(Intent.ACTION_SEND);
            intent3.setType("text/plain");
            String shareBody = textViewNote.getText().toString();
            String shareSub = "Notes for course: " + getTitle();
            intent3.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            intent3.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent3, "Share using"));
        });

        ////////// Term is passed in by ID so I am checking ID and displaying termTitle in a text view
        int termId = intent.getIntExtra(EXTRA_TERM_ID, -1);
        TermViewModel tViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        tViewModel.getTermById(termId).observe(this, termList -> {
            if (termList != null) {
                for (Term t : termList) {
                    String termTitle = t.getTermTitle();
                    textViewTerm.setText(termTitle);
                }
            }
        });

        //////////setting recycler view for mentor list//////////
        RecyclerView viewMentor = findViewById(R.id.view_course_detail_mentors);
        viewMentor.setLayoutManager(new LinearLayoutManager(this));
        viewMentor.setHasFixedSize(true);
        final MentorAdapter mAdapter = new MentorAdapter();
        viewMentor.setAdapter(mAdapter);

        MentorViewModel mViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mViewModel.getMentorById(mentorId).observe(this, mentors -> {
            mAdapter.setMentorList(mentors);
            mAdapter.notifyDataSetChanged();
        });
        mAdapter.setOnItemClickListener(mentor -> {
            Intent intent5 = new Intent(CourseDetailActivity.this, MentorDetailActivity.class);
            intent5.putExtra(EXTRA_MENTOR_ID, mentor.getMentorId());
            intent5.putExtra(EXTRA_NAME, mentor.getName());
            intent5.putExtra(EXTRA_PHONE_NUMBER, mentor.getPhoneNumber());
            intent5.putExtra(EXTRA_EMAIL, mentor.getEmail());
            startActivityForResult(intent5, ADD_MENTOR_REQUEST);
        });

        ////////// Setting Recycler view for assessment List //////////
        int courseId = intent.getIntExtra(EXTRA_ID, -1);
        RecyclerView viewAssessment = findViewById(R.id.view_course_detail_assessments);
        viewAssessment.setLayoutManager(new LinearLayoutManager(this));
        viewAssessment.setHasFixedSize(true);

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        viewAssessment.setAdapter(assessmentAdapter);

        AssessmentViewModel assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAssessmentsByCourse(courseId).observe(this, assessments -> {
            assessmentAdapter.setAssessmentList(assessments);
            assessmentAdapter.notifyDataSetChanged();
        });
        assessmentAdapter.setOnItemClickListener(assessment -> {
            Intent intent6 = new Intent(CourseDetailActivity.this, AssessmentDetailActivity.class);
            intent6.putExtra(EXTRA_ASSESSMENT_ID, assessment.getAssessmentId());
            intent6.putExtra(EXTRA_ASSESSMENT_TITLE, assessment.getAssessmentTitle());
            intent6.putExtra(EXTRA_ASSESSMENT_DUE_DATE, Dates.dateToString( assessment.getDueDate()));
            intent6.putExtra(EXTRA_ASSESSMENT_TYPE, assessment.getAssessmentType());
            intent6.putExtra(EXTRA_COURSE_ID, assessment.getCourseId());
            startActivityForResult(intent6, OPEN_ASSESSMENT_DETAIL);
        });
    }

    ////////// updating course from edit screen //////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {

            String title = data.getStringExtra(EXTRA_TITLE);
            String startDate = data.getStringExtra(EXTRA_START_DATE);
            String endDate = data.getStringExtra(EXTRA_END_DATE);
            String status = data.getStringExtra(EXTRA_STATUS);
            String note = data.getStringExtra(EXTRA_NOTE);
            int termId = data.getIntExtra(EXTRA_TERM_ID, 0);

            ///// converting string  into dates to store in DB /////
            Date convertedStart = Dates.stringToDateConverter(startDate);
            Date convertedEnd = Dates.stringToDateConverter(endDate);

            Course course = new Course(title, convertedStart, convertedEnd, status, note, termId, mentorId);
            course.setId(id);
            courseViewModel.update(course);

            textViewTitle.setText(title);
            textViewStartDate.setText(startDate);
            textViewEndDate.setText(endDate);
            textViewStatus.setText(status);
            textViewNote.setText(note);

            ////////// Term is passed in by ID so I am checking ID and displaying termTitle in a text view
            TermViewModel tViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
            tViewModel.getTermById(termId).observe(this, termList -> {
                if (termList != null) {
                    for (Term t : termList) {
                        String termTitle = t.getTermTitle();
                        textViewTerm.setText(termTitle);
                    }
                }
            });

            Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Course not updated", Toast.LENGTH_SHORT).show();
    }

}



