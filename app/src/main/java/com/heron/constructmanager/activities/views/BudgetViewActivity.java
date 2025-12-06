package com.heron.constructmanager.activities.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heron.constructmanager.R;

public class BudgetViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg;
    TextView titleTextView, descTextView, valueTextView;
    String constructionUidStr, titleStr, descStr, valueStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_view);
        titleTextView = findViewById(R.id.budget_view_title_text);
        descTextView = findViewById(R.id.budget_view_desc_text);
        valueTextView = findViewById(R.id.budget_view_value_text);
        backArrowImg = findViewById(R.id.budget_view_back_arrow);
        context = this;

        if (getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            titleStr = getIntent().getStringExtra("title");
            descStr = getIntent().getStringExtra("desc");
            valueStr = "R$ " + String.valueOf(getIntent().getFloatExtra("value", 0f)).replace(".", ",");
        }

        titleTextView.setText(titleStr);
        descTextView.setText(descStr);
        valueTextView.setText(valueStr);

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}