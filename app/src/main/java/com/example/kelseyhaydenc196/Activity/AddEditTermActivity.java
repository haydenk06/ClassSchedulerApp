package com.example.kelseyhaydenc196.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.Utilities.Dates;

import java.util.Calendar;

public class AddEditTermActivity extends AppCompatActivity {
    public static final String EXTRA_TERM_ID = "com.example.KelseyHaydenC196.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.example.KelseyHaydenC196.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START = "com.example.KelseyHaydenC196.EXTRA_TERM_START";
    public static final String EXTRA_TERM_END = "com.example.KelseyHaydenC196.EXTRA_TERM_END";

    ///// used for dates(conversion and setting in window)
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private String dateFormattedStart = null;
    private String dateFormattedEnd = null;

    ///// used for editing a term information /////
    private EditText editTextTermTitle;
    private EditText editTextTermStart;
    private EditText editTextTermEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        ///// find views for editing
        editTextTermTitle = findViewById(R.id.edit_text_term_title);
        editTextTermStart = findViewById(R.id.edit_text_term_start);
        editTextTermEnd = findViewById(R.id.edit_text_term_end);

        if (intent.hasExtra(EXTRA_TERM_ID)) {
            setTitle("Edit Term");
            ///// Setting Title, start + end dates, status and notes /////
            editTextTermTitle.setText(intent.getStringExtra(EXTRA_TERM_TITLE));
            editTextTermStart.setText(intent.getStringExtra(EXTRA_TERM_START));
            editTextTermEnd.setText(intent.getStringExtra(EXTRA_TERM_END));
        } else {
            setTitle("Add Term");
        }
        ////////// Handles start date in date picker and showing to text view /////////
        findViewById(R.id.edit_text_term_start).setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditTermActivity.this, startDateSetListener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        startDateSetListener = (view, year, month, dayOfMonth) -> {
            dateFormattedStart = Dates.Date(year, month, dayOfMonth);
            editTextTermStart.setText(dateFormattedStart);

        };

        ////////// Handles end date in date picker and showing to text view  /////////
        findViewById(R.id.edit_text_term_end).setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditTermActivity.this, endDateSetListener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        endDateSetListener = (view, year, month, dayOfMonth) -> {
            dateFormattedEnd = Dates.Date(year, month, dayOfMonth);
            editTextTermEnd.setText(dateFormattedEnd);
        };
    }

    /////////// Saves Term Information //////////
    private void saveTerm() {
        String termTitle = editTextTermTitle.getText().toString();
        String termStart = editTextTermStart.getText().toString();
        String termEnd = editTextTermEnd.getText().toString();
        int termId = getIntent().getIntExtra(EXTRA_TERM_ID, -1);


        if (termTitle.trim().isEmpty() || termStart.trim().isEmpty() || termEnd.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start/end dates for the Term", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TERM_TITLE, termTitle);
        data.putExtra(EXTRA_TERM_START, termStart);
        data.putExtra(EXTRA_TERM_END, termEnd);
        data.putExtra(EXTRA_TERM_ID, termId);

        if (termId != -1) {
            data.putExtra(EXTRA_TERM_ID, termId);
        }
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
            saveTerm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}