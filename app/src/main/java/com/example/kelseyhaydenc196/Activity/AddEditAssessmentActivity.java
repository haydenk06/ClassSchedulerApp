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

import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.AlertController;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditAssessmentActivity extends AppCompatActivity {
    public static final String EXTRA_ASSESSMENT_ID = "com.example.KelseyHaydenC196.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_TITLE = "com.example.KelseyHaydenC196.EXTRA_ASSESSMENT_TITLE";
    public static final String EXTRA_ASSESSMENT_TYPE = "com.example.KelseyHaydenC196.EXTRA_ASSESSMENT_TYPE";
    public static final String EXTRA_ASSESSMENT_DUE_DATE = "com.example.KelseyHaydenC196.EXTRA_ASSESSMENT_DUE_DATE";
    public static final String EXTRA_COURSE_ID = "com.example.KelseyHaydenC196.EXTRA_COURSE_ID";

    ///// used for date(conversion and setting in window)
    private DatePickerDialog.OnDateSetListener dueDateSetListener;
    private String formattedDueDate = null;

    ///// used for editing a assessment information /////
    private EditText editTextAssessmentTitle;
    private EditText editTextDueDate;

    ///// Type Spinner /////
    private Spinner sType;
    private List<String> sTypeList = new ArrayList<>();

    ///// course spinner ////
    private Spinner sCourse;
    private List<String> sCourseList = new ArrayList<>();
    private List<Course> listOfCourses = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        ///// find views for editing //////
        editTextAssessmentTitle = findViewById(R.id.edit_text_assessment_title);
        editTextDueDate = findViewById(R.id.edit_text_due_date);
        sType = findViewById(R.id.spinner_type);
        sCourse = findViewById(R.id.spinner_course);

        ///// adding 2 options for assessment type spinner /////
        sTypeList.add("Objective Assessment");
        sTypeList.add("Performance Assessment");

        ////////// Handles Assessment Type Spinner //////////
        ArrayAdapter<String> spinnerType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sTypeList);
        spinnerType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(spinnerType);

        ////////// Handling Course spinner menu //////////
        int courseId = intent.getIntExtra(EXTRA_COURSE_ID, -1);
        final ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sCourseList);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCourse.setAdapter(courseAdapter);

        final CourseViewModel courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        courseViewModel.getAllCourses().observe(this, courseList -> {
            if (courseList != null) {
                for (Course c : courseList) {
                    sCourseList.add(c.getCourseTitle());
                    listOfCourses.add(c);
                }
            }
            courseAdapter.notifyDataSetChanged();
            //This loop looks at the list of all mentors, checks mentorId for each one, if it matches the course we're editing,
            //finds the index in the spinner and sets spinner display to proper mentor..
            for (Course c : listOfCourses) {
                if (c.getId() == courseId) {
                    String courseTitle = c.getCourseTitle();
                    int spinnerSelection = getIndexM(sCourse, courseTitle);
                    sCourse.setSelection(spinnerSelection);
                }
            }
        });

        ////////// If statement handles change from add to edit assessment /////////
        if (intent.hasExtra(EXTRA_ASSESSMENT_ID)) {
            setTitle("Edit Assessment");
            ///// Setting title and due date for editing /////
            editTextAssessmentTitle.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TITLE));
            editTextDueDate.setText(intent.getStringExtra(EXTRA_ASSESSMENT_DUE_DATE));

            ///// Sets type spinner to type EXTRA that was passed in /////
            String type = intent.getStringExtra(EXTRA_ASSESSMENT_TYPE);
            for (String s : sTypeList) {
                assert type != null;
                if (type.equals(s)) {
                    int typeIndex = getIndexM(sType, type);
                    sType.setSelection(typeIndex);
                }
            }

        } else {
            setTitle("Add Assessment");
        }

        ////////// Handles start date in date picker and showing to text view /////////
        findViewById(R.id.edit_text_due_date).setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditAssessmentActivity.this, dueDateSetListener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        dueDateSetListener = (view, year, month, dayOfMonth) -> {
            formattedDueDate = Dates.Date(year, month, dayOfMonth);
            editTextDueDate.setText(formattedDueDate);
        };

    }

    /////////// Saves Assessment Information //////////
    private void saveAssessment() {
        String aTitle = editTextAssessmentTitle.getText().toString();
        String type = sType.getSelectedItem().toString();
        String dueDate = editTextDueDate.getText().toString();

        ///// Getting course selected from spinner as int /////
        int selectedCoursePosition = sCourse.getSelectedItemPosition();
        Course course = listOfCourses.get(selectedCoursePosition);
        int courseId = course.getId();

        if (aTitle.trim().isEmpty() || type.trim().isEmpty() || dueDate.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, type and due date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ASSESSMENT_TITLE, aTitle);
        data.putExtra(EXTRA_ASSESSMENT_TYPE, type);
        data.putExtra(EXTRA_ASSESSMENT_DUE_DATE, dueDate);
        data.putExtra(EXTRA_COURSE_ID, courseId);

        int assessmentId = getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, -1);
        if (assessmentId != -1) {
            data.putExtra(EXTRA_ASSESSMENT_ID, assessmentId);
        }

        ///// set alarm for due date /////
        AlertController alertController;
        alertController = new AlertController(aTitle + " due date is Today!",
                Dates.stringToDateConverter(dueDate), this);
        alertController.setAlert();

        setResult(RESULT_OK, data);
        finish();
    }

    ///// display menu /////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    ///// menu items //////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.button_save) {
            saveAssessment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////// getting type spinner selected item //////////
    private int getIndexM(@NotNull Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                return i;
            }
        }
        return -1;
    }
}
