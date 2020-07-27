package com.example.kelseyhaydenc196.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.AlertController;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.MentorViewModel;
import com.example.kelseyhaydenc196.ViewModel.TermViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_END_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_MENTOR_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NOTE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_START_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_STATUS;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TITLE;


public class EditCourseActivity extends AppCompatActivity {/*
    public static final String EXTRA_ID = "com.example.KelseyHaydenC196.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.KelseyHaydenC196.EXTRA_TITLE";
    public static final String EXTRA_START_DATE = "com.example.KelseyHaydenC196.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "com.example.KelseyHaydenC196.EXTRA_END_DATE";
    public static final String EXTRA_STATUS = "com.example.KelseyHaydenC196.EXTRA_STATUS";
    public static final String EXTRA_NOTE = "com.example.KelseyHaydenC196.EXTRA_NOTE";
    public static final String EXTRA_MENTOR_ID = "com.example.KelseyHaydenC196.EXTRA_MENTOR_ID";
    public static final String EXTRA_TERM_ID = "com.example.KelseyHaydenC196.EXTRA_TERM_ID";
    public static final String EXTRA_ASSESSMENT_ID = "com.example.KelseyHaydenC196.EXTRA_ASSESSMENT_ID";*/

    ///// used for dates(conversion and setting in window)
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private String dateFormattedStart = null;
    private String dateFormattedEnd = null;

    private EditText editTextTitle;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private EditText editTextNote;

    ///// Status Spinner /////
    private Spinner sCourseStatus;
    private List<String> sCourseStatusList = new ArrayList<>();

    ///// Term Spinner /////
    private Spinner sCourseTerm;
    private List<String> sCourseTermList = new ArrayList<>();
    private List<Term> listOfTerms = new ArrayList<>();

    ///// Mentor Spinner /////
    private Spinner sCourseMentor;
    private List<String> sCourseMentorList = new ArrayList<>();
    private List<Mentor> listOfMentors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        ///// find views for editing
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextStartDate = findViewById(R.id.edit_text_start_date);
        editTextEndDate = findViewById(R.id.edit_text_end_date);
        editTextNote = findViewById(R.id.edit_text_note);
        sCourseStatus = findViewById(R.id.spinner_course_status);
        sCourseTerm = findViewById(R.id.spinner_course_term);
        sCourseMentor = findViewById(R.id.spinner_course_mentor);

        ///// Setting Title, start + end dates, status and notes /////
        editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        editTextStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
        editTextEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
        editTextNote.setText(intent.getStringExtra(EXTRA_NOTE));

        ///// adding 4 options for course status. /////
        sCourseStatusList.add("In Progress");
        sCourseStatusList.add("Completed");
        sCourseStatusList.add("Dropped");
        sCourseStatusList.add("Planned");

        ////////// Handles start date in date picker and showing to text view /////////
        findViewById(R.id.edit_text_start_date).setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditCourseActivity.this, startDateSetListener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        startDateSetListener = (view, year, month, dayOfMonth) -> {
            dateFormattedStart = Dates.Date(year, month, dayOfMonth);
            editTextStartDate.setText(dateFormattedStart);
        };

        ////////// Handles end date in date picker and showing to text view  /////////
        findViewById(R.id.edit_text_end_date).setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditCourseActivity.this, endDateSetListener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        endDateSetListener = (view, year, month, dayOfMonth) -> {
            dateFormattedEnd = Dates.Date(year, month, dayOfMonth);
            editTextEndDate.setText(dateFormattedEnd);
        };

        //////////Handles Course Status Spinner//////////
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sCourseStatusList);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCourseStatus.setAdapter(statusAdapter);

        ///// Sets spinner to status EXTRA that was passed in /////
        String courseStatus = intent.getStringExtra(EXTRA_STATUS);
        for (String s : sCourseStatusList) {
            assert courseStatus != null;
            if (courseStatus.equals(s)) {
                int statusIndex = getIndexM(sCourseStatus, courseStatus);
                sCourseStatus.setSelection(statusIndex);
            }
        }

        ////////// Handling Term Spinner //////////
        int termId = intent.getIntExtra(EXTRA_TERM_ID, -1);
        final ArrayAdapter<String> termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sCourseTermList);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCourseTerm.setAdapter(termAdapter);

        final TermViewModel termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        termViewModel.getAllTerms().observe(this, termList -> {
            if (termList != null) {
                for (Term t : termList) {
                    sCourseTermList.add(t.getTermTitle());
                    listOfTerms.add(t);
                }
            }
            termAdapter.notifyDataSetChanged();
            //This loop looks at the list of all terms, checks termId for each one, if it matches the course we're editing,
            //finds the index in the spinner and sets spinner display proper term.
            for (Term t : listOfTerms) {
                if (t.getTermId() == termId) {
                    String termTitle = t.getTermTitle();
                    int spinnerSelection = getIndexM(sCourseTerm, termTitle);
                    sCourseTerm.setSelection(spinnerSelection);
                }
            }
        });

        ////////// Handling Mentor spinner menu //////////
        int mentorId = intent.getIntExtra(EXTRA_MENTOR_ID, -1);
        final ArrayAdapter<String> mentorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sCourseMentorList);
        mentorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCourseMentor.setAdapter(mentorAdapter);

        final MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);

        mentorViewModel.getAllMentors().observe(this, mentorList -> {
            if (mentorList != null) {
                for (Mentor m : mentorList) {
                    sCourseMentorList.add(m.getName());
                    listOfMentors.add(m);
                }
            }
            mentorAdapter.notifyDataSetChanged();
            //This loop looks at the list of all mentors, checks mentorId for each one, if it matches the course we're editing,
            //finds the index in the spinner and sets spinner display to proper mentor..
            for (Mentor m : listOfMentors) {
                if (m.getMentorId() == mentorId) {
                    String mentorName = m.getName();
                    int spinnerSelection = getIndexM(sCourseMentor, mentorName);
                    sCourseMentor.setSelection(spinnerSelection);
                }
            }
        });
    }

    ///////// updates course changes //////////
    private void saveCourse() {
        String title = editTextTitle.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();
        String status = sCourseStatus.getSelectedItem().toString();
        String note = editTextNote.getText().toString();
        int courseId = getIntent().getIntExtra(EXTRA_ID, -1);

        ///// Getting term position from spinner as int/////
        int selectedTermPosition = sCourseTerm.getSelectedItemPosition();
        Term term = listOfTerms.get(selectedTermPosition);
        int termId = term.getTermId();

        /// Getting mentor selected from spinner as int /////
        int selectedMentorPosition = sCourseMentor.getSelectedItemPosition();
        Mentor mentor = listOfMentors.get(selectedMentorPosition);
        int mentorId = mentor.getMentorId();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields (note = optional) and pick a start and end date ", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_START_DATE, startDate);
        data.putExtra(EXTRA_END_DATE, endDate);
        data.putExtra(EXTRA_STATUS, status);
        data.putExtra(EXTRA_NOTE, note);
        data.putExtra(EXTRA_MENTOR_ID, mentorId);
        data.putExtra(EXTRA_TERM_ID, termId);

        if (courseId != -1) {
            data.putExtra(EXTRA_ID, courseId);
        }

        ///// sets alarms for course start date /////
        AlertController alertController;
        alertController = new AlertController(title + " course starts Today!",
                Dates.stringToDateConverter(startDate), this);
        alertController.setAlert();

        ///// sets alarms for course end date /////
        alertController = new AlertController(title + " course ends Today! ",
                Dates.stringToDateConverter(endDate), this);
        alertController.setAlert();


        setResult(RESULT_OK, data);
        finish();
    }

    ////////// display menu //////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    ///// menu items /////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.button_save) {
            saveCourse();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////// getting spinner selected item //////////
    private int getIndexM(@NotNull Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                return i;
            }
        }
        return -1;
    }
}

