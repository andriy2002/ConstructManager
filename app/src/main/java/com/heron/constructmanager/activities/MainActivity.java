package com.heron.constructmanager.activities;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.heron.constructmanager.Constants;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.animations.LoadingAnimation;
import com.heron.constructmanager.activities.forms.SignUpFormActivity;

public class MainActivity extends Activity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    String emailStr, pwStr;

    ValidateInput validateInput;

    EditText signInEmail, signInPassword;
    TextView createAccText;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccText = findViewById(R.id.create_account_txt);
        signInButton = findViewById(R.id.sign_in_button);
        signInEmail = findViewById(R.id.sign_in_email);
        signInPassword = findViewById(R.id.sign_in_password);
        validateInput = new ValidateInput(MainActivity.this, signInEmail, signInPassword);

        // Init firebase Auth
        auth = FirebaseAuth.getInstance();

        createAccText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpFormActivity.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAcc();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, Constants.PLEASE_LOGIN, Toast.LENGTH_LONG).show();
        }

    }

    public void signInAcc() {
        boolean email_verified = validateInput.validateEmail();
        boolean pw_verified = validateInput.validatePassword();

        if (email_verified && pw_verified) {
            emailStr = signInEmail.getText().toString().trim();
            pwStr = signInPassword.getText().toString().trim();

            auth.signInWithEmailAndPassword(emailStr, pwStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, Constants.UNEXPECTED_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    }

}