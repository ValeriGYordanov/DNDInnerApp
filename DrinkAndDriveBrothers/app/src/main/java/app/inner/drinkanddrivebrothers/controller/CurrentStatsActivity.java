package app.inner.drinkanddrivebrothers.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.Course;
import app.inner.drinkanddrivebrothers.recyclerview.CurrentStatsRecycler;
import app.inner.drinkanddrivebrothers.utility.Util;

public class CurrentStatsActivity extends AppCompatActivity {

    private String driver1Names, driver2Names, date;
    private ArrayList<Course> allCourses = new ArrayList<>();
    private DatabaseReference ref;
    private RecyclerView recycler;
    private Button btnBack;
    private CurrentStatsRecycler recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_stats);

        recycler = findViewById(R.id.curr_stats_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btn_recycler_curr_stats_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        driver1Names = getIntent().getStringExtra("driver1Names");
        driver2Names = getIntent().getStringExtra("driver2Names");
        date = getIntent().getStringExtra("date");


        ref = Util.getFirebaseReference();
        ref.child("courses").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                for (DataSnapshot data : childrens) {
                    Course course = data.getValue(Course.class);
                    if((course.getDriver1().equals(driver1Names) || course.getDriver1().equals(driver2Names))  &&  (course.getDriver2().equals(driver1Names) || course.getDriver2().equals(driver2Names))){
                        allCourses.add(course);
                    }
                }
                recyclerAdapter = new CurrentStatsRecycler(CurrentStatsActivity.this,allCourses);

                recycler.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
