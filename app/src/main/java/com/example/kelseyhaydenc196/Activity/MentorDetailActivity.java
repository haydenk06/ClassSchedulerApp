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
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;
import com.example.kelseyhaydenc196.ViewModel.MentorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_EMAIL;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_END_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_MENTOR_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NAME;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NOTE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_PHONE_NUMBER;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_START_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_STATUS;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TITLE;

public class MentorDetailActivity extends AppCompatActivity {
    public static final int EDIT_MENTOR_REQUEST = 2;
    public static final int OPEN_COURSE_DETAIL = 3;

    private MentorViewModel mentorViewModel;

    ///// used for setting  mentor information /////
    private TextView textViewName;
    private TextView textViewPhoneNumber;
    private TextView textViewEmail;

    private int mentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mentorViewModel = new ViewModelProvider(this).get(MentorViewModel.class);
        setContentView(R.layout.activity_detail_mentor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        ////////// Finding view items for displaying //////////
        textViewName = findViewById(R.id.text_view_name);
        textViewPhoneNumber = findViewById(R.id.text_view_phone);
        textViewEmail = findViewById(R.id.text_view_email);

        textViewName.setText(intent.getStringExtra(EXTRA_NAME));
        textViewPhoneNumber.setText(intent.getStringExtra(EXTRA_PHONE_NUMBER));
        textViewEmail.setText(intent.getStringExtra(EXTRA_EMAIL));

        mentorId = intent.getIntExtra(EXTRA_MENTOR_ID, -1);

        ////////// button to open add/edit mentor window //////////
        FloatingActionButton buttonEditMentor = findViewById(R.id.fab_edit_mentor);
        buttonEditMentor.setOnClickListener(v -> {
            Intent intent2 = new Intent(MentorDetailActivity.this, AddEditMentorActivity.class);
            intent2.putExtra(EXTRA_NAME, textViewName.getText());
            intent2.putExtra(EXTRA_PHONE_NUMBER, textViewPhoneNumber.getText());
            intent2.putExtra(EXTRA_EMAIL, textViewEmail.getText());
            intent2.putExtra(EXTRA_MENTOR_ID, mentorId);
            //intent2.putExtra(EditCourseActivity.EXTRA_ID, courseID);

            startActivityForResult(intent2, EDIT_MENTOR_REQUEST);
        });

        ////////// button to open email to mentor  //////////
        FloatingActionButton buttonEmail = findViewById(R.id.fab_email);
        buttonEmail.setOnClickListener(v -> sendMail());

        ////////// Setting Recycler view for course List //////////
        RecyclerView viewCourses = findViewById(R.id.view_mentor_detail_courses);
        viewCourses.setLayoutManager(new LinearLayoutManager(this));
        viewCourses.setHasFixedSize(true);
        final CourseAdapter cAdapter = new CourseAdapter();
        viewCourses.setAdapter(cAdapter);

        CourseViewModel cViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        cViewModel.getCoursesByMentor(mentorId).observe(this, course -> {
            cAdapter.setCourseList(course);
            cAdapter.notifyDataSetChanged();
        });
        cAdapter.setOnItemClickListener(course -> {
            Intent intent3 = new Intent(MentorDetailActivity.this, CourseDetailActivity.class);
            intent3.putExtra(EXTRA_ID, course.getId());
            intent3.putExtra(EXTRA_MENTOR_ID, course.getMentorId());
            intent3.putExtra(EXTRA_TITLE, course.getCourseTitle());
            intent3.putExtra(EXTRA_START_DATE,  Dates.dateToString(course.getStartDate()));
            intent3.putExtra(EXTRA_END_DATE,  Dates.dateToString(course.getEndDate()));
            intent3.putExtra(EXTRA_NOTE, course.getNote());
            intent3.putExtra(EXTRA_STATUS, course.getStatus());
            startActivityForResult(intent3, OPEN_COURSE_DETAIL);
        });
    }

    ////////// updating mentor from edit screen //////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_MENTOR_REQUEST && resultCode == RESULT_OK) {

            String name = data.getStringExtra(EXTRA_NAME);
            String phoneNumber = data.getStringExtra(EXTRA_PHONE_NUMBER);
            String email = data.getStringExtra(EXTRA_EMAIL);

            Mentor mentor = new Mentor(name, phoneNumber, email);
            mentor.setMentorId(mentorId);
            mentorViewModel.update(mentor);

            textViewName.setText(name);
            textViewPhoneNumber.setText(phoneNumber);
            textViewEmail.setText(email);

            Toast.makeText(this, "Mentor Updated", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Mentor not updated", Toast.LENGTH_SHORT).show();
    }

    ////////// Handles opening an email to mentor //////////
    private void sendMail() {
        String recipientList = textViewEmail.getText().toString().trim();
        String[] recipients = recipientList.split(",");
        Intent intent4 = new Intent(Intent.ACTION_SEND);
        intent4.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent4.setType("message/rfc822");

        ///// catches errors if there is no email client is available or no internet etc.../////
        try {
            startActivity(Intent.createChooser(intent4, "Choose an email client"));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
