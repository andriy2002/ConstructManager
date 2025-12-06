package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heron.constructmanager.R;

public class UpdatePasswordFormActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    String newPasswordStr, repeatPasswordStr;

    ImageView backArrowImg;
    Button updatePasswordButton;
    EditText newPwEditText, repeatPwEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        backArrowImg = findViewById(R.id.update_password_back_arrow);
        updatePasswordButton = findViewById(R.id.update_password_button);
        newPwEditText = findViewById(R.id.update_password_new_password);
        repeatPwEditText = findViewById(R.id.update_password_repeat_password);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPasswordStr = newPwEditText.getText().toString().trim();
                repeatPasswordStr = repeatPwEditText.getText().toString().trim();
                if (user != null) {
                    if (newPasswordStr.equals(repeatPasswordStr)) {
                        user.updatePassword(newPasswordStr);
                        Toast.makeText(UpdatePasswordFormActivity.this, "Пароль успішно оновлено.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(UpdatePasswordFormActivity.this, "Введені паролі не однакові.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}