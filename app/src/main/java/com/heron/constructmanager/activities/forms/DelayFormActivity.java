package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.activities.views.ScheduleViewActivity;
import com.heron.constructmanager.models.Schedule;
import com.heron.constructmanager.service.ResponsabilityService;
import com.heron.constructmanager.service.UserService;

import java.util.List;

public class DelayFormActivity extends AppCompatActivity {

    ImageView backArrowImg;
    EditText titleEditText, reasonEditText;
    Button nextButton;
    Switch excusableSwitch, compensableSwitch, concurrentSwitch, criticalSwitch;

    String constructionUidStr, scheduleUidStr, titleStr, reasonStr;
    boolean isExcusable, isCompensable, isConcurrent, isCritical;

    ValidateInput validateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_form);
        titleEditText = findViewById(R.id.delay_form_title);
        reasonEditText = findViewById(R.id.delay_form_reason);
        nextButton = findViewById(R.id.delay_form_next_button);
        backArrowImg = findViewById(R.id.delay_form_back_arrow);

        excusableSwitch = findViewById(R.id.delay_form_excusable_switch);
        compensableSwitch = findViewById(R.id.delay_form_compensable_switch);
        concurrentSwitch = findViewById(R.id.delay_form_concurrent_switch);
        criticalSwitch = findViewById(R.id.delay_form_critical_switch);

        validateInput = new ValidateInput(DelayFormActivity.this, titleEditText, reasonEditText);

        if (getIntent().getExtras() != null) {
            // ALWAYS WILL GET THESE INTENTS
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            scheduleUidStr = getIntent().getStringExtra("scheduleUid");
        } else {
            finish();
        }

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infosVerified()) {
                    getComponentsContent();
                    Intent intent = new Intent(DelayFormActivity.this, DelayFinishFormActivity.class);
                    intent = putExtrasDelay(intent, titleStr, reasonStr, isExcusable, isCompensable, isConcurrent, isCritical);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        finish();
    }

    private Intent putExtrasDelay(Intent intent, String title, String reason, boolean isExcusable, boolean isCompensable, boolean isConcurrent, boolean isCritical) {
        intent.putExtra("constructionUid", constructionUidStr);
        intent.putExtra("scheduleUid", scheduleUidStr);
        intent.putExtra("title", title);
        intent.putExtra("reason", reason);
        intent.putExtra("isExcusable", isExcusable);
        intent.putExtra("isCompensable", isCompensable);
        intent.putExtra("isConcurrent", isConcurrent);
        intent.putExtra("isCritical", isCritical);
        return intent;
    }

    public boolean infosVerified() {
        boolean titleVerified = validateInput.validateTitle();
        boolean reasonVerified = validateInput.validateReason();
        return titleVerified && reasonVerified;
    }

    public void getComponentsContent() {
        titleStr = titleEditText.getText().toString().trim();
        reasonStr = reasonEditText.getText().toString().trim();
        isExcusable = excusableSwitch.isActivated();
        isCompensable = compensableSwitch.isActivated();
        isConcurrent = concurrentSwitch.isActivated();
        isCritical = criticalSwitch.isActivated();
    }


}