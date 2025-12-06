package com.heron.constructmanager.activities.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heron.constructmanager.R;

public class ConstructionFinishedViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg;
    TextView titleTextView, stageTextView, infoTextView, budgetTextView, scheduleTextView, photoTextView, mapTextView, responsiblesTextView;
    String titleStr, stageStr, constructionUidStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction_finished_view);
        backArrowImg = findViewById(R.id.construction_finished_view_back_arrow);
        titleTextView = findViewById(R.id.construction_finished_view_title_text);
        stageTextView = findViewById(R.id.construction_finished_view_stage_text);
        infoTextView = findViewById(R.id.construction_finished_view_info_text);
        budgetTextView = findViewById(R.id.construction_finished_view_budget_text);
        scheduleTextView = findViewById(R.id.construction_finished_view_schedule_text);
        photoTextView = findViewById(R.id.construction_finished_view_photo_text);
        mapTextView = findViewById(R.id.construction_finished_view_map_text);
        responsiblesTextView = findViewById(R.id.construction_finished_view_responsbiles_text);
        context = this;

        if (getIntent().getExtras() != null) {
            titleStr = getIntent().getStringExtra("title");
            stageStr = getIntent().getStringExtra("stage");
            constructionUidStr = getIntent().getStringExtra("constructionUid");

            titleTextView.setText(titleStr);
            stageTextView.setText(stageStr);
        }

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}