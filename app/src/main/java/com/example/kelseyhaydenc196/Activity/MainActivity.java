package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.ViewModel.AssessmentViewModel;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;
import com.example.kelseyhaydenc196.ViewModel.MentorViewModel;
import com.example.kelseyhaydenc196.ViewModel.TermViewModel;


public class MainActivity extends AppCompatActivity {
    public static final int OPEN_TERM_REQUEST = 1;
    public static final int OPEN_COURSE_REQUEST = 2;
    public static final int OPEN_MENTOR_REQUEST = 3;
    public static final int OPEN_ASSESSMENT_REQUEST = 4;

    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;
    private MentorViewModel mentorViewModel;
    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///// implementing buttons and setting click actions /////
        Button bTerm = findViewById(R.id.button_term);
        Button bCourses = findViewById(R.id.button_courses);
        Button bMentors = findViewById(R.id.button_mentors);
        Button bAssessments = findViewById(R.id.button_assessments);

        ///// implementing view models to add/delete data /////
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        ///// setting onClick listeners for each button /////
        bTerm.setOnClickListener(v -> {
            Intent tIntent = new Intent(MainActivity.this, TermActivity.class);
            startActivityForResult(tIntent, OPEN_TERM_REQUEST);
        });
        bCourses.setOnClickListener(v -> {
            Intent cIntent = new Intent(MainActivity.this, CourseActivity.class);
            startActivityForResult(cIntent, OPEN_COURSE_REQUEST);
        });
        bMentors.setOnClickListener(v -> {
            Intent mIntent = new Intent(MainActivity.this, MentorActivity.class);
            startActivityForResult(mIntent, OPEN_MENTOR_REQUEST);
        });
        bAssessments.setOnClickListener(v -> {
            Intent aIntent = new Intent(MainActivity.this, AssessmentActivity.class);
            startActivityForResult(aIntent, OPEN_ASSESSMENT_REQUEST);
        });
    }

    ///////// loading main menu item //////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //////////  Menu Options //////////
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
           if(id == R.id.add_sample_data){
               addData();
               return true;
           } else if(id == R.id.delete_current_data){
               deleteData();
               return true;
           }
        return super.onOptionsItemSelected(item);
    }

    ////////// Handles adding sample data for menu item //////////
    public void addData(){
        mentorViewModel.addSampleMentors();
        assessmentViewModel.addSampleAssessments();
        termViewModel.addSampleTerms();
        courseViewModel.addSampleCourses();
    }

    ////////// Handles deleting all data for menu item //////////
    public void deleteData(){
        mentorViewModel.deleteAllMentors();
        assessmentViewModel.deleteAllAssessments();
        termViewModel.deleteAllTerms();
        courseViewModel.deleteAllCourses();
    }

}


