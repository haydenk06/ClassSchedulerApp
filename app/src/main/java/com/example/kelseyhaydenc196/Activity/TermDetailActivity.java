package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.CourseAdapter;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;
import com.example.kelseyhaydenc196.ViewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_END_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NOTE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_START_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_STATUS;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_END;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_START;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_TITLE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TITLE;

public class TermDetailActivity extends AppCompatActivity {
    public static final int EDIT_TERM_REQUEST = 2;
    public static final int OPEN_COURSE_DETAIL = 3;

    private TermViewModel termViewModel;



    ///// used for setting  term information /////
    private TextView textViewTermTitle;
    private TextView textViewTermStart;
    private TextView textViewTermEnd;

    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        setContentView(R.layout.activity_detail_term);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        ////////// Finding view items for displaying //////////
        textViewTermTitle = findViewById(R.id.text_view_term_title);
        textViewTermStart = findViewById(R.id.text_view_term_start);
        textViewTermEnd = findViewById(R.id.text_view_term_end);

        textViewTermTitle.setText(intent.getStringExtra(EXTRA_TERM_TITLE));
        textViewTermStart.setText(intent.getStringExtra(EXTRA_TERM_START));
        textViewTermEnd.setText(intent.getStringExtra(EXTRA_TERM_END));

        termId = intent.getIntExtra(EXTRA_TERM_ID, -1);

        FloatingActionButton buttonEditTerm = findViewById(R.id.fab_edit_term);
        buttonEditTerm.setOnClickListener(v -> {
            Intent intent2 = new Intent(TermDetailActivity.this, AddEditTermActivity.class);
            intent2.putExtra(EXTRA_TERM_TITLE, textViewTermTitle.getText());
            intent2.putExtra(EXTRA_TERM_START, textViewTermStart.getText());
            intent2.putExtra(EXTRA_TERM_END, textViewTermEnd.getText());
            intent2.putExtra(EXTRA_TERM_ID, termId);

            startActivityForResult(intent2, EDIT_TERM_REQUEST);
        });

        ////////// Setting Recycler view for course List //////////
        RecyclerView viewCourses = findViewById(R.id.view_term_detail_courses);
        viewCourses.setLayoutManager(new LinearLayoutManager(this));
        viewCourses.setHasFixedSize(true);
        final CourseAdapter cAdapter = new CourseAdapter();
        viewCourses.setAdapter(cAdapter);

        CourseViewModel cViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        cViewModel.getCoursesByTerm(termId).observe(this, courses -> {
            cAdapter.setCourseList(courses);
            cAdapter.notifyDataSetChanged();
        });
        cAdapter.setOnItemClickListener(course -> {
            Intent intent4 = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
            intent4.putExtra(EXTRA_ID, course.getId());
            intent4.putExtra(EXTRA_TITLE, course.getCourseTitle());
            intent4.putExtra(EXTRA_START_DATE,  Dates.dateToString(course.getStartDate()));
            intent4.putExtra(EXTRA_END_DATE,  Dates.dateToString(course.getEndDate()));
            intent4.putExtra(EXTRA_NOTE, course.getNote());
            intent4.putExtra(EXTRA_STATUS, course.getStatus());
            startActivityForResult(intent4, OPEN_COURSE_DETAIL);
        });
    }

    ////////// Updating Term from edit screen //////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK) {

            String termTitle = data.getStringExtra(EXTRA_TERM_TITLE);
            String termStart = data.getStringExtra(EXTRA_TERM_START);
            String termEnd = data.getStringExtra(EXTRA_TERM_END);

            ///// converting string  into dates to store in DB /////
            Date convertedStart = Dates.stringToDateConverter(termStart);
            Date convertedEnd = Dates.stringToDateConverter(termEnd);

            Term term = new Term(termTitle, convertedStart, convertedEnd);
            term.setTermId(termId);
            termViewModel.update(term);

            textViewTermTitle.setText(termTitle);
            textViewTermStart.setText(termStart);
            textViewTermEnd.setText(termEnd);

            Toast.makeText(this, "Term Updated", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Term not updated", Toast.LENGTH_SHORT).show();
    }
}
