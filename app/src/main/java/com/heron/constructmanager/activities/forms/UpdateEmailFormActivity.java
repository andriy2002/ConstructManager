package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;

public class UpdateEmailFormActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    String newEmailStr;

    ValidateInput validate;

    ImageView backArrowImg;
    EditText currentEmailEditText, newEmailEditText;
    TextView sendVerificationEmailText;
    Button updateEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_email);
        // Components
        backArrowImg = findViewById(R.id.update_email_back_arrow);
        currentEmailEditText = findViewById(R.id.update_email_current_email);
        newEmailEditText = findViewById(R.id.update_email_new_email);
        updateEmailButton = findViewById(R.id.update_email_button);
        sendVerificationEmailText = findViewById(R.id.send_verification_email_txt);
        // Db objects
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        // Validation
        validate = new ValidateInput(UpdateEmailFormActivity.this, newEmailEditText);

        setCurrentEmail();

        // Listeners
        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean email_verified = validate.validateEmail();

                if (email_verified && user != null) {
                    newEmailStr = newEmailEditText.getText().toString().trim();
                    user.updateEmail(newEmailStr);
                    Toast.makeText(UpdateEmailFormActivity.this, Constants.EMAIL_UPDATED, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UpdateEmailFormActivity.this, Constants.EMAIL_INVALID, Toast.LENGTH_LONG).show();
                }
            }
        });

        sendVerificationEmailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isEmailVerified()) {
                    Toast.makeText(UpdateEmailFormActivity.this, Constants.EMAIL_ALREADY_VERIFIED, Toast.LENGTH_LONG).show();
                } else {
                    user.sendEmailVerification();
                    Toast.makeText(UpdateEmailFormActivity.this, Constants.EMAIL_VERIF_SEND, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setCurrentEmail(){
        if (user != null) {
            currentEmailEditText.setEnabled(true);
            currentEmailEditText.setText(user.getEmail());
            currentEmailEditText.setEnabled(false);
        }
        else {
            Toast.makeText(this, Constants.PLEASE_LOGIN, Toast.LENGTH_LONG).show();
        }
    }

}