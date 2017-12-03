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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
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

    private EditText txtPhoneNumber;
    private TextView txtKM, txtPrice;
    private Button btnFinishDrive, btnStartDrive, btnFinishShift, btnShowCourses;
    private DatabaseReference ref;
    private String driver1Names, driver2Names, kmString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_work);

        driver1Names = getIntent().getStringExtra("driver1");
        driver2Names = getIntent().getStringExtra("driver2");

        txtPhoneNumber = findViewById(R.id.txt_driver_work_clientNumber);
        txtKM = findViewById(R.id.txt_driver_work_km);
        txtPrice = findViewById(R.id.txt_driver_work_price);
        //btnFinishDrive = findViewById(R.id.btn_driver_work_finishDrive);
        btnStartDrive = findViewById(R.id.btn_driver_work_startDrive);
        btnFinishShift = findViewById(R.id.btn_driver_finish_shift);
        btnShowCourses = findViewById(R.id.btn_driver_open_courses);
        btnListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && data != null){
            if(resultCode == 0){
                float m = 0;
                m = data.getExtras().getFloat("distance");
                float km = m/1000;
                int kmInt = Math.round(km);
                double price = Util.getPriceFromKM(kmInt);
                if (km == 0) {
                    txtKM.setText("Изминато разстояние от последия курс: 0 км");
                }else {
                    kmString = String.format("%.03f", km);
                    txtKM.setText("Изминато разстояние от последия курс: " + kmInt + " км");
                    saveCourseData();
                    txtPhoneNumber.setText("");
                }
                txtPrice.setText("Цена: " + String.valueOf(price) + " лева");
            }
        }
    }

    private void btnListeners() {
//        btnStartDrive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // get date and time form firebase
//            }
//        });

        btnStartDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPhoneNumber.getText().toString().trim().isEmpty()){
                    Toast.makeText(DriverWorkActivity.this, "Въведете номер", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(DriverWorkActivity.this,MapActivity.class);
                startActivityForResult(intent,0);
            }
        });

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

//        btnFinishDrive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // edna stabilna proverka na poletata trqbva tuk :D
//                saveCourseData();
//                txtPhoneNumber.setText("");
//                txtKM.setText("");
//            }
//        });
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

        float kilos = Float.valueOf(kmString);
        int km = Math.round(kilos);
        Course course = new Course(txtPhoneNumber.getText().toString(), km, driver1Names, driver2Names, hour, Util.getPriceFromKM(km));
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
