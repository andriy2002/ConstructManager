package com.heron.constructmanager.activities.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.lists.ListBudgetsActivity;
import com.heron.constructmanager.activities.lists.ListPhotosActivity;
import com.heron.constructmanager.activities.lists.ListResponsabilitiesActivity;
import com.heron.constructmanager.activities.lists.ListSchedulesActivity;
import com.heron.constructmanager.service.ConstructionService;

import java.util.ArrayList;

public class ConstructionExecViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg, deleteImg, newStageImg, cancelImg;
    CardView infoCard, budgetCard, scheduleCard, photoCard, mapCard, responsabilitiesCard;
    TextView titleTextView, stageTextView, infoTextView, budgetTextView, scheduleTextView, photoTextView, mapTextView, responsiblesTextView;
    String titleStr, stageStr, addressStr, typeStr, constructionUidStr;
    ArrayList<String> responsiblesEmailList;
    ConstructionService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction_exec_view);
        context = this;
        responsiblesEmailList = new ArrayList<>();
        service = new ConstructionService(this);

        backArrowImg = findViewById(R.id.construction_exec_view_back_arrow);
        titleTextView = findViewById(R.id.construction_exec_view_title_text);
        stageTextView = findViewById(R.id.construction_exec_view_stage_text);
        infoCard  = findViewById(R.id.construction_exec_view_info_card);
        budgetCard  = findViewById(R.id.construction_exec_budget_card);
        scheduleCard  = findViewById(R.id.construction_exec_schedule_card);
        photoCard  = findViewById(R.id.construction_exec_photo_card);
        mapCard  = findViewById(R.id.construction_exec_map_card);
        responsabilitiesCard  = findViewById(R.id.construction_exec_responsabilities_card);
        deleteImg = findViewById(R.id.construction_exec_view_delete);
        newStageImg = findViewById(R.id.construction_exec_view_new_stage);
        cancelImg = findViewById(R.id.construction_exec_view_cancel);

        if(getIntent().getExtras() != null) {
            titleStr = getIntent().getStringExtra("title");
            stageStr = getIntent().getStringExtra("stage");
            addressStr = getIntent().getStringExtra("address");
            typeStr = getIntent().getStringExtra("type");
            responsiblesEmailList = getIntent().getStringArrayListExtra("responsibles");
            constructionUidStr = getIntent().getStringExtra("constructionUid");

            titleTextView.setText(titleStr);
            stageTextView.setText(stageStr);
        }

        infoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConstructionExecViewActivity.this, InfoViewActivity.class);
                intent = putExtrasConstruction(intent, titleStr, addressStr, stageStr, typeStr, constructionUidStr);
                startActivity(intent);
            }
        });

        budgetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConstructionExecViewActivity.this, ListBudgetsActivity.class);
                intent = putExtrasConstruction(intent, titleStr, addressStr, stageStr, typeStr, constructionUidStr);
                startActivity(intent);
            }
        });

        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConstructionExecViewActivity.this, ListSchedulesActivity.class);
                intent = putExtrasConstruction(intent, titleStr, addressStr, stageStr, typeStr, constructionUidStr);
                startActivity(intent);
            }
        });

        photoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConstructionExecViewActivity.this, ListPhotosActivity.class);
                intent = putExtrasConstruction(intent, titleStr, addressStr, stageStr, typeStr, constructionUidStr);
                startActivity(intent);
            }
        });

        mapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConstructionExecViewActivity.this, MapViewActivity.class);
                intent = putExtrasConstruction(intent, titleStr, addressStr, stageStr, typeStr, constructionUidStr);
                startActivity(intent);
            }
        });

        responsabilitiesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConstructionExecViewActivity.this, ListResponsabilitiesActivity.class);
                intent = putExtrasConstruction(intent, titleStr, addressStr, stageStr, typeStr, constructionUidStr);
                startActivity(intent);
            }
        });


        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConstructionExecViewActivity.this);
                builder.setTitle("Видалити роботу");
                builder.setMessage("Ви впевнені, що хочете видалити цю роботу?");
                AlertDialog dialog = builder.create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        service.deleteConstruction(constructionUidStr);
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

        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConstructionExecViewActivity.this);
                builder.setTitle("Скасувати проєкт");
                builder.setMessage("Ви впевнені, що хочете скасувати проєкт?");
                AlertDialog dialog = builder.create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        service.cancelConstruction(constructionUidStr);
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

        newStageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConstructionExecViewActivity.this);
                builder.setTitle("Перехід на наступний етап");
                builder.setMessage("Ви впевнені, що хочете перевести етап будівництва на \"Завершено\"?");
                AlertDialog dialog = builder.create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        service.advanceStageToFinished(constructionUidStr);
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

    }

    private Intent putExtrasConstruction(Intent intent, String title, String address, String stage, String type, String constructionUid) {
        intent.putExtra("title", title);
        intent.putExtra("address", address);
        intent.putExtra("stage", stage);
        intent.putExtra("type", type);
        intent.putExtra("constructionUid", constructionUid);
        intent.putStringArrayListExtra("responsibles", responsiblesEmailList);
        return intent;
    }
}