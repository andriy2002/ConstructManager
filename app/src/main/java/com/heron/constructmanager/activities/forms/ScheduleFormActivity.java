package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.service.ResponsabilityService;
import com.heron.constructmanager.service.ScheduleService;
import com.heron.constructmanager.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFormActivity extends AppCompatActivity {

    ImageView backArrowImg;
    EditText titleEditText, descEditText, deadlineEditText;
    Button addButton;

    ScheduleService scheduleService;
    ValidateInput validateInput;

    String constructionUidStr, titleStr, deadlineStr, stateStr, scheduleUidStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateStr = "У встановлений термін";
        setContentView(R.layout.activity_schedule_form);
        backArrowImg = findViewById(R.id.schedule_form_back_arrow);
        titleEditText = findViewById(R.id.schedule_form_title);
        deadlineEditText = findViewById(R.id.schedule_form_deadline);
        addButton = findViewById(R.id.schedule_form_add_button);
        scheduleService = new ScheduleService(this);

        if(getIntent().getExtras() != null) {
            // SHOULD ALWAYS GET THESE EXTRAS
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            // WILL GET THESE EXTRAS ONLY IF EDIT
            stateStr = (getIntent().getStringExtra("state") != null) ? getIntent().getStringExtra("state") : stateStr;
            scheduleUidStr = getIntent().getStringExtra("scheduleUid");
            titleStr = getIntent().getStringExtra("title");
            deadlineStr = getIntent().getStringExtra("deadline");
        }
        titleEditText.setText(titleStr);
        deadlineEditText.setText(deadlineStr);

        validateInput = new ValidateInput(ScheduleFormActivity.this, titleEditText, deadlineEditText);

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
                    scheduleService.writeSchedule(constructionUidStr, titleStr, deadlineStr, stateStr, scheduleUidStr);
                    finish();
                }
            }
        });

    }

    public boolean infosVerified() {
        boolean titleVerified = validateInput.validateTitle();
        boolean deadlineVerified = validateInput.validateDeadline();
        return titleVerified && deadlineVerified;
    }

    public void getEditTextsContent() {
        titleStr = titleEditText.getText().toString().trim();
        deadlineStr = deadlineEditText.getText().toString().trim();
    }
}