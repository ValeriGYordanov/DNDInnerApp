package app.inner.drinkanddrivebrothers.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.Course;
import app.inner.drinkanddrivebrothers.utility.Util;

public class DriverWorkActivity extends AppCompatActivity {

    private EditText txtPhoneNumber, txtKM;
    private Button btnFinishDrive, btnStartDrive;
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
        btnStartDrive = findViewById(R.id.btn_driver_work_startDrive);
        btnListeners();
    }

    private void btnListeners() {
        btnStartDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get date and time form firebase
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
    }

    private void saveCourseData() {
        Course course = new Course(txtPhoneNumber.getText().toString(), Float.parseFloat(txtKM.getText().toString()), driver1Names, driver2Names);
        ref = Util.getFirebaseReference();
        ref.child("courses").push().setValue(course);
    }
}
