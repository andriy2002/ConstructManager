package com.heron.constructmanager.activities.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.forms.ReponsabilityFormActivity;
import com.heron.constructmanager.service.ResponsabilityService;

public class ResponsabilityOpenViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg, editImg, solveImg;
    ResponsabilityService responsabilityService;
    TextView titleTextView, stateTextView, descTextView, deadlineTextView, responsibleEmailTextView;
    String constructionUidStr, responsabilityUidStr, titleStr, stateStr, descStr, deadlineStr, responsibleEmailStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsability_open_view);
        titleTextView = findViewById(R.id.responsability_open_view_title_text);
        stateTextView = findViewById(R.id.responsability_open_view_state_text);
        descTextView = findViewById(R.id.responsability_open_view_desc_text);
        deadlineTextView = findViewById(R.id.responsability_open_view_deadline_text);
        responsibleEmailTextView = findViewById(R.id.responsability_open_view_responsible_email_text);
        backArrowImg = findViewById(R.id.responsability_open_view_back_arrow);
        editImg = findViewById(R.id.responsability_open_view_edit);
        solveImg = findViewById(R.id.responsability_open_view_solve_img);
        context = this;
        responsabilityService = new ResponsabilityService(this);

        if (getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            responsabilityUidStr = getIntent().getStringExtra("responsabilityUid");
            titleStr = getIntent().getStringExtra("title");
            stateStr = getIntent().getStringExtra("state");
            descStr = getIntent().getStringExtra("desc");
            deadlineStr = getIntent().getStringExtra("deadline");
            responsibleEmailStr = getIntent().getStringExtra("responsibleEmail");
        }

        titleTextView.setText(titleStr);
        stateTextView.setText(stateStr);
        descTextView.setText(descStr);
        deadlineTextView.setText(deadlineStr);
        responsibleEmailTextView.setText(responsibleEmailStr);

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        solveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResponsabilityOpenViewActivity.this);
                builder.setTitle("Вирішити відповідальність");
                builder.setMessage("Чи була відповідальність виконана та завершена?");
                AlertDialog dialog = builder.create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responsabilityService.solveResponsability(constructionUidStr, responsabilityUidStr);
                        finish();
                    }
                });
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "Ні", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReponsabilityFormActivity.class);
                intent.putExtra("constructionUid", constructionUidStr);
                intent.putExtra("responsabilityUid", responsabilityUidStr);
                intent.putExtra("title", titleStr);
                intent.putExtra("state", stateStr);
                intent.putExtra("desc", descStr);
                intent.putExtra("deadline", deadlineStr);
                intent.putExtra("responsibleEmail", responsibleEmailStr);
                context.startActivity(intent);
            }
        });
    }
}