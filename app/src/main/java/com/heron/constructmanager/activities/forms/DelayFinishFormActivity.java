package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.service.ScheduleService;

public class DelayFinishFormActivity extends AppCompatActivity {

    ImageView backArrowImg;
    EditText aditionalInfoEditText, daysEditText;
    Button addButton;

    ScheduleService service;

    String constructionUidStr, scheduleUidStr, titleStr, reasonStr, aditionalInfoStr, delayUidStr;
    boolean isExcusable, isCompensable, isConcurrent, isCritical;
    int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_finish_form);
        aditionalInfoEditText = findViewById(R.id.delay_finish_form_aditional_info);
        daysEditText = findViewById(R.id.delay_finish_form_days);
        addButton = findViewById(R.id.delay_finish_form_add_button);
        backArrowImg = findViewById(R.id.delay_finish_form_back_arrow);
        service = new ScheduleService(this);

        if (getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            scheduleUidStr = getIntent().getStringExtra("scheduleUid");
            titleStr = getIntent().getStringExtra("title");
            reasonStr = getIntent().getStringExtra("reason");
            isExcusable = getIntent().getBooleanExtra("isExcusable", false);
            isCompensable = getIntent().getBooleanExtra("isCompensable", false);
            isConcurrent = getIntent().getBooleanExtra("isConcurrent", false);
            isCritical = getIntent().getBooleanExtra("isCritical", false);
        }

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getComponentsContent();
                service.writeDelay(constructionUidStr, scheduleUidStr, titleStr, reasonStr,isExcusable, isCompensable, isConcurrent, isCritical, days, aditionalInfoStr, delayUidStr);
                finish();
            }
        });

    }

    public void getComponentsContent() {
        aditionalInfoStr = aditionalInfoEditText.getText().toString().trim();
        days = Integer.valueOf(daysEditText.getText().toString().trim());
    }

}