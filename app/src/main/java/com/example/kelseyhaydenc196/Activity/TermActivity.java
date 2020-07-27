package com.example.kelseyhaydenc196.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.Model.Term;
import com.example.kelseyhaydenc196.R;
import com.example.kelseyhaydenc196.TermAdapter;
import com.example.kelseyhaydenc196.Utilities.Dates;
import com.example.kelseyhaydenc196.ViewModel.CourseViewModel;
import com.example.kelseyhaydenc196.ViewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_END;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_ID;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_START;
import static com.example.kelseyhaydenc196.Utilities.Constants.EXTRA_TERM_TITLE;


public class TermActivity extends AppCompatActivity {
    public static final int ADD_TERM_REQUEST = 1;
    public static final int EDIT_TERM_REQUEST = 2;

    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ///// button to add a new term /////
        FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(v -> {
            Intent intent = new Intent(TermActivity.this, AddEditTermActivity.class);
            startActivityForResult(intent, ADD_TERM_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, adapter::setTermList);

        ///// swipe to delete term /////
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            ///// drag and drop /////
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            ///// swiping to delete /////
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ///// checks to see if a course is attached to term and deletes only if no courses are present /////
                Term checkForCourses = adapter.getTermAt(viewHolder.getAdapterPosition());
                int termId = checkForCourses.getTermId();

                Executor executor = Executors.newSingleThreadExecutor();
                CourseViewModel courseViewModel = ViewModelProviders.of(TermActivity.this).get(CourseViewModel.class);
                executor.execute(() -> {
                    Looper.prepare();
                    int test = courseViewModel.getCourseCountByTerm(termId);
                    if (test > 0) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        Toast.makeText(TermActivity.this, "There are courses in this term.", Toast.LENGTH_SHORT).show();
                    } else {
                        termViewModel.delete(checkForCourses);
                        Toast.makeText(TermActivity.this, "Term Deleted", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                });
            }
        }).attachToRecyclerView(recyclerView);
        ///// used for clicking on item /////
        adapter.setOnItemClickListener(term -> {
            ////// loading data to edit term /////
            Intent intent = new Intent(TermActivity.this, TermDetailActivity.class);
            intent.putExtra(EXTRA_TERM_ID, term.getTermId());
            intent.putExtra(EXTRA_TERM_TITLE, term.getTermTitle());
            intent.putExtra(EXTRA_TERM_START, Dates.dateToString(term.getTermStart()));
            intent.putExtra(EXTRA_TERM_END, Dates.dateToString(term.getTermEnd()));
            startActivityForResult(intent, EDIT_TERM_REQUEST);
        });
    }

    ////////// Saving Term Information  from add screen //////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            String termTitle = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_TITLE);
            String termStart = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_START);
            String termEnd = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_END);

            ///// converting string  into dates to store in DB /////
            Date convertedStart = Dates.stringToDateConverter(termStart);
            Date convertedEnd = Dates.stringToDateConverter(termEnd);

            Term term = new Term(termTitle, convertedStart, convertedEnd);
            termViewModel.insert(term);

            Toast.makeText(this, "New Term Saved", Toast.LENGTH_SHORT).show();
        }
    }
}


