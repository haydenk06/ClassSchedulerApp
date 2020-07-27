package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.AssessmentAdapter;
import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.AssessmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_DUE_DATE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_TITLE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_ASSESSMENT_TYPE;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_COURSE_ID;


public class AssessmentActivity extends AppCompatActivity {
    public static final int ADD_ASSESSMENT_REQUEST = 1;
    public static final int EDIT_ASSESSMENT_REQUEST = 2;

    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ///// button to add a new assessment /////
        FloatingActionButton buttonAddAssessment = findViewById(R.id.button_add_assessment);
        buttonAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentActivity.this, AddEditAssessmentActivity.class);
                startActivityForResult(intent, ADD_ASSESSMENT_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AssessmentAdapter adapter = new AssessmentAdapter();
        recyclerView.setAdapter(adapter);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, adapter::setAssessmentList);

        ///// swipe to delete assessment /////
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            ///// drag and drop /////
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            ///// swiping to delete /////
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                assessmentViewModel.delete(adapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AssessmentActivity.this, "Assessment Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        // used for clicking on item and loading detail screen
        adapter.setOnItemClickListener(assessment -> {
            Intent intent = new Intent(AssessmentActivity.this, AssessmentDetailActivity.class);
            //loading data to detail Assessment screen
            intent.putExtra(EXTRA_ASSESSMENT_ID, assessment.getAssessmentId());
            intent.putExtra(EXTRA_ASSESSMENT_TITLE, assessment.getAssessmentTitle());
            intent.putExtra(EXTRA_ASSESSMENT_TYPE, assessment.getAssessmentType());
            intent.putExtra(EXTRA_ASSESSMENT_DUE_DATE, Dates.dateToString(assessment.getDueDate()));
            intent.putExtra(EXTRA_COURSE_ID, assessment.getCourseId());
            startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
        });
    }

    ////////// Saving from Add Assessment screen//////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            String assessmentTitle = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TITLE);
            String assessmentType = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE);
            String dueDate = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE);
            int courseId = data.getIntExtra(AddEditAssessmentActivity.EXTRA_COURSE_ID, -1);

            ///// converting string  into date to store in DB /////
            Date convertedDueDate = Dates.stringToDateConverter(dueDate);

            Assessment assessment = new Assessment(assessmentTitle, assessmentType, convertedDueDate, courseId);
            assessmentViewModel.insert(assessment);

            Toast.makeText(this, "New Assessment Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
