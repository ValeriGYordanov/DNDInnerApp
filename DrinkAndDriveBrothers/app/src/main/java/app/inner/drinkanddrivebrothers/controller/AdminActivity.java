package app.inner.drinkanddrivebrothers.controller;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.model.User;
import app.inner.drinkanddrivebrothers.utility.Util;

public class AdminActivity extends AppCompatActivity {

    private Button btnRegisterUser, btnStatistic;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnRegisterUser = findViewById(R.id.btn_admin_register);
        btnStatistic = findViewById(R.id.btn_admin_statistic);

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Регистрация на потребител");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputUsername = new EditText(this);
        inputUsername.setHint("Име");
        inputUsername.setMaxLines(1);
        layout.addView(inputUsername);

        final EditText inputSurname = new EditText(this);
        inputSurname.setHint("Фамилия");
        inputSurname.setMaxLines(1);
        layout.addView(inputSurname);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // da se testva i za drugi validacii
                if (inputSurname.getText().toString().matches("[a-zA-Z ]+") && inputUsername.getText().toString().matches("[a-zA-Z ]+")) {
                    User user = new User(inputUsername.getText().toString(), inputSurname.getText().toString());
                    registerUser(user);
                } else {
                    Toast.makeText(AdminActivity.this, "Невалидни полета...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void registerUser(User user) {
        ref = Util.getFirebaseReference();
        ref.child("drivers").push().setValue(user);
    }
}
