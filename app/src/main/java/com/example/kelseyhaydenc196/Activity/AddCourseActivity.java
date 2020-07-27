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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_END_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_MENTOR_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NOTE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_START_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_STATUS;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TITLE;

public class AddCourseActivity extends AppCompatActivity {
    ///// used for dates(conversion and setting in window) /////
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private String dateFormattedStart = null;
    private String dateFormattedEnd = null;

    ///// used for entering course information //////
    private EditText editTextTitle;
    private EditText editTextNote;
    private EditText editTextStartDate;
    private EditText editTextEndDate;

    ///// Status spinner /////
    private Spinner sCourseStatus;
    private static List<String> sCourseStatusList = new ArrayList<>();

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
        setContentView(R.layout.activity_add_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        /////finding view items for adding course /////
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextStartDate = findViewById(R.id.edit_text_start_date);
        editTextEndDate = findViewById(R.id.edit_text_end_date);
        sCourseStatus = findViewById(R.id.spinner_course_status);
        sCourseTerm = findViewById(R.id.spinner_course_term);
        sCourseMentor = findViewById(R.id.spinner_course_mentor);
        editTextNote = findViewById(R.id.edit_text_note);

        /////adding options for status spinner/////
        sCourseStatusList.add("In-Progress");
        sCourseStatusList.add("Completed");
        sCourseStatusList.add("Dropped");
        sCourseStatusList.add("Planned");

        ////////// Handles start date in date picker and showing to text view /////////
        findViewById(R.id.edit_text_start_date).setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddCourseActivity.this, startDateSetListener,
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
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddCourseActivity.this, endDateSetListener,
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

        //////////Handles Course Status Spinner///////////
        ArrayAdapter<String> spinnerStatus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sCourseStatusList);
        spinnerStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCourseStatus.setAdapter(spinnerStatus);

        ////////// Handles filling term spinner with currently available terms //////////
        ArrayAdapter<String> spinnerTerms = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sCourseTermList);
        spinnerTerms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCourseTerm.setAdapter(spinnerTerms);

        TermViewModel termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, termList -> {
            if (termList != null) {
                for (Term t : termList) {
                    sCourseTermList.add(t.getTermTitle());
                    listOfTerms.add(t);
                }
            }
            spinnerTerms.notifyDataSetChanged();
        });

        ////////// Getting allMentors List and adding to spinner //////////
        ArrayAdapter<String> spinnerMentors = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sCourseMentorList);
        spinnerMentors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCourseMentor.setAdapter(spinnerMentors);

        MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getAllMentors().observe(this, mentorList -> {
            if (mentorList != null) {
                for (Mentor m : mentorList) {
                    sCourseMentorList.add(m.getName());
                    listOfMentors.add(m);
                }
            }
            spinnerMentors.notifyDataSetChanged();
        });
    }

    /////////// Saves Course Information //////////
    private void saveCourse() {
        String title = editTextTitle.getText().toString();
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();
        String status = sCourseStatus.getSelectedItem().toString();
        String note = editTextNote.getText().toString();

        ///// Getting term position in list and selecting termID
        int selectedTermPosition = sCourseTerm.getSelectedItemPosition();
        Term term = listOfTerms.get(selectedTermPosition);
        int termId = term.getTermId();

        //// /Getting mentor selected from spinner as int /////
        int selectedMentorPosition = sCourseMentor.getSelectedItemPosition();
        Mentor mentor = listOfMentors.get(selectedMentorPosition);
        int mentorId = mentor.getMentorId();

        ///// checking if information was put into fields /////
        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields(notes = optional) and pick a start and end date ", Toast.LENGTH_SHORT).show();
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
}