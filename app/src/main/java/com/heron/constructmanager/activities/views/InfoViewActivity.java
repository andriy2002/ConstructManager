package com.heron.constructmanager.activities.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heron.constructmanager.R;
import com.heron.constructmanager.service.ConstructionService;

import java.util.ArrayList;

import static java.lang.String.join;

public class InfoViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg;
    TextView titleTextView, stageTextView, addressTextView, responsiblesTextView, typeTextView;
    String titleStr, stageStr, addressStr, responsiblesStr, typeStr, constructionUidStr;
    ArrayList<String> responsiblesEmailList;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);
        backArrowImg = findViewById(R.id.construction_exec_info_view_back_arrow);
        titleTextView = findViewById(R.id.construction_exec_info_view_title_text);
        stageTextView = findViewById(R.id.construction_exec_info_view_stage_text);
        addressTextView = findViewById(R.id.construction_exec_info_view_address_text);
        typeTextView = findViewById(R.id.construction_exec_info_view_type_text);
        responsiblesTextView = findViewById(R.id.construction_exec_info_view_responsibles_text);
        responsiblesEmailList = new ArrayList<>();
        context = this;

        if(getIntent().getExtras() != null) {
            titleStr = getIntent().getStringExtra("title");
            stageStr = getIntent().getStringExtra("stage");
            addressStr = getIntent().getStringExtra("address");
            typeStr = getIntent().getStringExtra("type");
            responsiblesEmailList = getIntent().getStringArrayListExtra("responsibles");
            constructionUidStr = getIntent().getStringExtra("constructionUid");
        }

        titleTextView.setText(titleStr);
        stageTextView.setText(stageStr);
        addressTextView.setText(addressStr);
        typeTextView.setText(typeStr);
        responsiblesStr = join(", ", responsiblesEmailList);
        responsiblesTextView.setText(responsiblesStr);

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}