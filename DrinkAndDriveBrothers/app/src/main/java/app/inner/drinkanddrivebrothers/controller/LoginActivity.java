package app.inner.drinkanddrivebrothers.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.utility.Util;

public class LoginActivity extends AppCompatActivity {

    private Button btnStartWork, btnAdminLogin;
    private final String usernameAdmin = "a";
    private final String passwordAdmin = "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnStartWork = findViewById(R.id.btn_start_work);
        //Util.getDateAndTimeFormFirebase();
        btnStartWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextAct = new Intent(LoginActivity.this, DriverActivity.class);
                startActivity(nextAct);
                finish();
            }
        });
        
        btnAdminLogin = findViewById(R.id.btn_admin_login);
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вход");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputUsername = new EditText(this);
        inputUsername.setHint("потребител");
        inputUsername.setMaxLines(1);
        inputUsername.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        layout.addView(inputUsername);

        final EditText inputPassword = new EditText(this);
        inputPassword.setHint("парола");
        inputPassword.setMaxLines(1);
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(inputPassword);

        builder.setView(layout);

        builder.setPositiveButton("Продължи", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(inputUsername.getText().toString().equals(usernameAdmin) && inputPassword.getText().toString().equals(passwordAdmin)){
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Грешно потребителско име или парола", Toast.LENGTH_SHORT).show();
                    inputPassword.setText("");
                }
            }
        });

        builder.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
