package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.activities.MainActivity;
import com.heron.constructmanager.animations.LoadingAnimation;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.activities.HomeActivity;
import com.heron.constructmanager.models.User;
import com.heron.constructmanager.service.UserService;

public class SignUpFormActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference db;
    private FirebaseUser user;
    UserService service;

    String emailStr, pwStr;
    boolean admin;

    EditText signUpEmailEditText, signUpPwEditText, signUpRepeatPwEditText;
    ImageView backArrowImg;
    Button signUpButton;
    Spinner spinner;

    ValidateInput valiteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Components
        signUpEmailEditText = findViewById(R.id.sign_up_email);
        signUpPwEditText = findViewById(R.id.sign_up_password);
        signUpRepeatPwEditText = findViewById(R.id.sign_up_repeat_password);
        backArrowImg = findViewById(R.id.sign_up_back_arrow);
        spinner = findViewById(R.id.sign_up_admin_spinner);
        signUpButton = findViewById(R.id.sign_up_button);

        // Firebase Auth
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        service = new UserService(this);

        // Listeners
        valiteInput = new ValidateInput(SignUpFormActivity.this, signUpEmailEditText, signUpPwEditText, signUpRepeatPwEditText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usertypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpNewAcc();
            }
        });


    }

    public void signUpNewAcc() {
        boolean emailVerified = valiteInput.validateEmail();
        boolean pwVerified = valiteInput.validatePassword();
        boolean repeatPwVerified = valiteInput.validateRepeatPassword();

        if (emailVerified && pwVerified && repeatPwVerified) {

            emailStr = signUpEmailEditText.getText().toString().trim();
            pwStr = signUpPwEditText.getText().toString().trim();
            admin = (spinner.getSelectedItem().toString().trim().equals(Constants.CONTRACTOR));

            auth.createUserWithEmailAndPassword(emailStr, pwStr)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = auth.getCurrentUser();
                                onCompleteSuccess(user, admin);
                            } else {
                                Toast.makeText(SignUpFormActivity.this, Constants.UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void onCompleteSuccess(FirebaseUser user, boolean admin) {

        // 1. Перевіряємо email
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            Toast.makeText(this, "Email користувача відсутній!", Toast.LENGTH_LONG).show();
            Log.e("SignUpDebug", "Email користувача null!");
            return;
        }

        // 2. Логуємо дані користувача
        String username = usernameFromEmail(user.getEmail());
        Log.d("SignUpDebug", "Creating user: UID=" + user.getUid() + ", name=" + username + ", email=" + user.getEmail() + ", admin=" + admin);

        // 3. Запис користувача
        service.writeNewUser(user.getUid(), username, user.getEmail(), admin, false);

        // Надсилання листа підтвердження
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpFormActivity.this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpFormActivity.this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                        Log.e("SignUpDebug", "Failed to send verification email", task.getException());
                    }
                });

        Intent intent = new Intent(SignUpFormActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}