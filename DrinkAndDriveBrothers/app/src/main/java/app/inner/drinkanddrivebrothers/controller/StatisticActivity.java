package app.inner.drinkanddrivebrothers.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.Course;
import app.inner.drinkanddrivebrothers.recyclerview.StatisticsRecView;
import app.inner.drinkanddrivebrothers.utility.Util;


public class StatisticActivity extends AppCompatActivity {

    private ArrayList<Course> allCourses;
    private DatabaseReference ref;
    private RecyclerView statisticRecView;
    private StatisticsRecView recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        statisticRecView = findViewById(R.id.statistics_list);
        LinearLayoutManager recLayMan = new LinearLayoutManager(this);
        statisticRecView.setLayoutManager(recLayMan);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statisticRecView.getContext(),
                recLayMan.getOrientation());

        statisticRecView.addItemDecoration(dividerItemDecoration);

        String date = getIntent().getStringExtra("By");
        if (date == null || date.isEmpty()){
            getCourses();
        }else{
            getCourses(date);
        }
    }

    private void getCourses(String date) {
        allCourses = new ArrayList<>();
        ref = Util.getFirebaseReference();
        ref.child("courses").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                for (DataSnapshot data : childrens) {
                    Course course = data.getValue(Course.class);
                    allCourses.add(course);
                }

                recyclerViewAdapter = new StatisticsRecView(StatisticActivity.this, allCourses);
                statisticRecView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void getCourses() {
        allCourses = new ArrayList<>();
        ref = Util.getFirebaseReference();
        ref.child("courses").child(Util.getFormatedDay()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                for (DataSnapshot data : childrens) {
                    Course course = data.getValue(Course.class);
                    allCourses.add(course);
                }

                recyclerViewAdapter = new StatisticsRecView(StatisticActivity.this, allCourses);
                statisticRecView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
