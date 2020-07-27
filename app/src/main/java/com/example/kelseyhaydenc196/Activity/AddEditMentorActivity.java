package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kelseyhaydenc196.R;

public class AddEditMentorActivity extends AppCompatActivity {
    public static final String EXTRA_MENTOR_ID = "com.example.KelseyHaydenC196.EXTRA_MENTOR_ID";
    public static final String EXTRA_NAME = "com.example.KelseyHaydenC196.EXTRA_NAME";
    public static final String EXTRA_PHONE_NUMBER = "com.example.KelseyHaydenC196.EXTRA_PHONE_NUMBER";
    public static final String EXTRA_EMAIL = "com.example.KelseyHaydenC196.EXTRA_EMAIL";
    public static final int EDIT_MENTOR_REQUEST = 1;

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mentor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();

        editTextName = findViewById(R.id.edit_text_name);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextEmail = findViewById(R.id.edit_text_email);

        if (intent.hasExtra(EXTRA_MENTOR_ID)) {
            setTitle("Edit Mentor ");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextPhoneNumber.setText(intent.getStringExtra(EXTRA_PHONE_NUMBER));
            editTextEmail.setText(intent.getStringExtra(EXTRA_EMAIL));
        } else {
            setTitle("Add Mentor");
        }
    }

    ////////// Saves Mentor Information //////////
    private void saveMentor() {
        String name = editTextName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();

        if (name.trim().isEmpty() || phoneNumber.trim().isEmpty() || email.trim().isEmpty()) {
            Toast.makeText(this, "Please fill out ALL fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);
        data.putExtra(EXTRA_EMAIL, email);

        int id = getIntent().getIntExtra(EXTRA_MENTOR_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_MENTOR_ID, id);
        }
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
        switch (item.getItemId()) {
            case R.id.button_save:
                saveMentor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
