package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.animations.LoadingAnimation;
import com.heron.constructmanager.models.User;
import com.heron.constructmanager.service.ResponsabilityService;
import com.heron.constructmanager.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ReponsabilityFormActivity extends AppCompatActivity {

    Spinner spinner;
    ImageView backArrowImg;
    EditText titleEditText, descEditText, deadlineEditText;
    Button addButton;

    List<String> emailsList;

    UserService userService;
    ResponsabilityService responsabilityService;

    String constructionUidStr, titleStr, descStr, responsibleEmailStr, deadlineStr, stateStr, responsabilityUidStr;

    ValidateInput validateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateStr = Constants.OPEN;
        setContentView(R.layout.activity_reponsability_form);
        spinner = findViewById(R.id.responsability_form_responsible_spinner);
        backArrowImg = findViewById(R.id.responsability_form_back_arrow);
        titleEditText = findViewById(R.id.responsability_form_title);
        descEditText = findViewById(R.id.responsability_form_desc);
        deadlineEditText = findViewById(R.id.responsability_form_deadline);
        addButton = findViewById(R.id.responsability_form_add_button);
        emailsList = new ArrayList<>();
        userService = new UserService(this);
        responsabilityService = new ResponsabilityService(this);

        if(getIntent().getExtras() != null) {
            // SHOULD ALWAYS GET THESE EXTRAS
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            // WILL GET THESE EXTRAS ONLY IF EDIT
            stateStr = (getIntent().getStringExtra("state") != null) ? getIntent().getStringExtra("state") : stateStr;
            responsabilityUidStr = getIntent().getStringExtra("responsabilityUid");
            responsibleEmailStr = getIntent().getStringExtra("responsibleEmail");
            titleStr = getIntent().getStringExtra("title");
            descStr = getIntent().getStringExtra("desc");
            deadlineStr = getIntent().getStringExtra("deadline");
        }
        titleEditText.setText(titleStr);
        descEditText.setText(descStr);
        deadlineEditText.setText(deadlineStr);

        validateInput = new ValidateInput(ReponsabilityFormActivity.this, titleEditText, descEditText, deadlineEditText, spinner);

        // Listeners
        DatabaseReference usersReference = userService.getUsersReference();
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String email = childSnapshot.child("email").getValue(String.class);
                    emailsList.add(email);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ReponsabilityFormActivity.this, android.R.layout.simple_spinner_item, emailsList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infosVerified()) {
                    getEditTextsContent();
                    responsabilityService.writeResponsability(constructionUidStr, responsabilityUidStr, titleStr, descStr, deadlineStr, stateStr, responsibleEmailStr);
                    finish();
                }
            }
        });

    }

    public boolean infosVerified() {
        boolean titleVerified = validateInput.validateTitle();
        boolean descVerified = validateInput.validateDesc();
        boolean deadlineVerified = validateInput.validateDeadline();
        boolean spinnerVerified  = validateInput.validateSpinner();
        return titleVerified && descVerified && deadlineVerified && spinnerVerified;
    }

    public void getEditTextsContent() {
        titleStr = titleEditText.getText().toString().trim();
        descStr = descEditText.getText().toString().trim();
        deadlineStr = deadlineEditText.getText().toString().trim();
        responsibleEmailStr = spinner.getSelectedItem().toString().trim();
    }

}