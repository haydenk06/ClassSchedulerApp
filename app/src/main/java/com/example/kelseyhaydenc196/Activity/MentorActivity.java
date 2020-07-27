package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.MentorAdapter;
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.ViewModel.MentorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_EMAIL;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_MENTOR_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_NAME;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_PHONE_NUMBER;

public class MentorActivity extends AppCompatActivity {
    public static final int ADD_MENTOR_REQUEST = 1;
    public static final int EDIT_MENTOR_REQUEST = 2;

    private MentorViewModel mentorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ///// button to add a new course /////
        FloatingActionButton buttonAddMentor = findViewById(R.id.button_add_mentor);
        buttonAddMentor.setOnClickListener(v -> {
            Intent intent = new Intent(MentorActivity.this, AddEditMentorActivity.class);
            startActivityForResult(intent, ADD_MENTOR_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final MentorAdapter adapter = new MentorAdapter();
        recyclerView.setAdapter(adapter);

        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getAllMentors().observe(this, adapter::setMentorList);

        ////////// swipe to delete mentor //////////
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            ///// drag and drop /////
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            ///// swiping to delete /////
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mentorViewModel.delete(adapter.getMentorAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MentorActivity.this, "Mentor deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        ///// used for clicking on item /////
        adapter.setOnItemClickListener(mentor -> {
            ///// loading data to mentor details screen /////
            Intent intent = new Intent(MentorActivity.this, MentorDetailActivity.class);
            intent.putExtra(EXTRA_MENTOR_ID, mentor.getMentorId());
            intent.putExtra(EXTRA_NAME, mentor.getName());
            intent.putExtra(EXTRA_PHONE_NUMBER, mentor.getPhoneNumber());
            intent.putExtra(EXTRA_EMAIL, mentor.getEmail());
            startActivityForResult(intent, EDIT_MENTOR_REQUEST);
        });
    }

    //////////// saving mentor information from add screen //////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_MENTOR_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditMentorActivity.EXTRA_NAME);
            String phoneNumber = data.getStringExtra(AddEditMentorActivity.EXTRA_PHONE_NUMBER);
            String email = data.getStringExtra(AddEditMentorActivity.EXTRA_EMAIL);

            Mentor mentor = new Mentor(name, phoneNumber, email);
            mentorViewModel.insert(mentor);

            Toast.makeText(this, "New Mentor Added", Toast.LENGTH_SHORT).show();
        }
    }
}
