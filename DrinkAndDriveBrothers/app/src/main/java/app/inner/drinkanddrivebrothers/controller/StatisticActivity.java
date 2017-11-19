package app.inner.drinkanddrivebrothers.controller;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private Button dailyStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        statisticRecView = findViewById(R.id.statistics_list);
        dailyStatistics = findViewById(R.id.day_statistics);
        dailyStatistics.setClickable(false);

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

        dailyStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double dailyKM = 0;
                double dailyMoney = 0;
                for (Course crs : allCourses){
                    dailyKM += crs.getKm();
                    dailyMoney += crs.getPrice();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(StatisticActivity.this);
                builder.setTitle("Статистика за деня!");

                LinearLayout layout = new LinearLayout(StatisticActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView allKilos = new TextView(StatisticActivity.this);
                allKilos.setTextColor(Color.BLACK);
                allKilos.setTextSize(15);
                allKilos.setText("Изминати Километри за деня : " + Double.toString(dailyKM) + " км.");
                layout.addView(allKilos);

                final TextView allMoney = new TextView(StatisticActivity.this);
                allMoney.setTextColor(Color.BLACK);
                allMoney.setTextSize(15);
                allMoney.setText("Оборот за деня : " + Double.toString(dailyMoney) + " лв.");
                layout.addView(allMoney);

                builder.setView(layout);
                builder.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

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
                dailyStatistics.setClickable(true);
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
                dailyStatistics.setClickable(true);
                recyclerViewAdapter = new StatisticsRecView(StatisticActivity.this, allCourses);
                statisticRecView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
