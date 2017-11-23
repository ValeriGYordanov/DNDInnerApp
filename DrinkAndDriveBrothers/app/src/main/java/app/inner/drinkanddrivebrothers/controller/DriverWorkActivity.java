package app.inner.drinkanddrivebrothers.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.Course;
import app.inner.drinkanddrivebrothers.utility.Util;

public class DriverWorkActivity extends AppCompatActivity {

    private EditText txtPhoneNumber, txtKM;
    private Button btnFinishDrive, btnStartDrive, btnFinishShift, btnShowCourses;
    private DatabaseReference ref;
    private String driver1Names, driver2Names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_work);

        driver1Names = getIntent().getStringExtra("driver1");
        driver2Names = getIntent().getStringExtra("driver2");

        txtPhoneNumber = findViewById(R.id.txt_driver_work_clientNumber);
        txtKM = findViewById(R.id.txt_driver_work_km);
        btnFinishDrive = findViewById(R.id.btn_driver_work_finishDrive);
        btnFinishShift = findViewById(R.id.btn_driver_finish_shift);
        btnShowCourses = findViewById(R.id.btn_driver_open_courses);
        btnListeners();
    }

    private void btnListeners() {
//        btnStartDrive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // get date and time form firebase
//            }
//        });

        btnShowCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day = Util.getFormatedDay();
                String hour = Util.getFormatedHour(DriverWorkActivity.this);

                String[] hourSplited = hour.split(":");
                char firstDigit = hourSplited[0].charAt(0);

                if(String.valueOf(firstDigit).equals("0")){
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    SimpleDateFormat previousDay = new SimpleDateFormat("yyyy-MM-dd");
                    day = previousDay.format(cal.getTime());
                }
                Intent intent = new Intent(DriverWorkActivity.this,CurrentStatsActivity.class);
                intent.putExtra("driver1Names",driver1Names);
                intent.putExtra("driver2Names",driver2Names);
                intent.putExtra("date",day);
                startActivity(intent);
            }
        });

        btnFinishDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // edna stabilna proverka na poletata trqbva tuk :D
                saveCourseData();
                txtPhoneNumber.setText("");
                txtKM.setText("");
            }
        });
        btnFinishShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverWorkActivity.this);
                builder.setTitle("Завърши Смяна?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                builder.setNegativeButton("Не", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Завърши Смяна?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("Не", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void saveCourseData() {
        String day = Util.getFormatedDay();
        String hour = Util.getFormatedHour(this);

        String[] hourSplited = hour.split(":");
        char firstDigit = hourSplited[0].charAt(0);

        if(String.valueOf(firstDigit).equals("0")){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            SimpleDateFormat previousDay = new SimpleDateFormat("yyyy-MM-dd");
            day = previousDay.format(cal.getTime());
        }

        int kilos = Integer.valueOf(txtKM.getText().toString());
        Course course = new Course(txtPhoneNumber.getText().toString(), Float.parseFloat(txtKM.getText().toString()), driver1Names, driver2Names, hour, Util.getPriceFromKM(kilos));
        ref = Util.getFirebaseReference();
        ref.child("courses").child(day).push().setValue(course);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(DriverWorkActivity.this, "Курсът е въведен успешно!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DriverWorkActivity.this, "Курсът НЕ Е ВЪВЕДЕН!", Toast.LENGTH_SHORT).show();
            }
        });
        //ref.child("courses").push().setValue(course);
    }
}
