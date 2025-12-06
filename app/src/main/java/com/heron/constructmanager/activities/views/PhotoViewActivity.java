package com.heron.constructmanager.activities.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heron.constructmanager.R;

public class PhotoViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg;
    TextView titleTextView, descTextView;
    String constructionUidStr, titleStr, descStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        titleTextView = findViewById(R.id.photo_view_title_text);
        descTextView = findViewById(R.id.photo_view_desc_text);
        backArrowImg = findViewById(R.id.photo_view_back_arrow);
        context = this;

        if (getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            titleStr = getIntent().getStringExtra("title");
            descStr = getIntent().getStringExtra("desc");
        }

        titleTextView.setText(titleStr);
        descTextView.setText(descStr);

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}