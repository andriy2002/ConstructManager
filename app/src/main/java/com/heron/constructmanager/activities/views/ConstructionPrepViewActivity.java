package com.heron.constructmanager.activities.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.HomeActivity;
import com.heron.constructmanager.activities.forms.ConstructionPrepFormActivity;
import com.heron.constructmanager.models.User;
import com.heron.constructmanager.service.ConstructionService;
import com.heron.constructmanager.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.join;

public class ConstructionPrepViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg, deleteImg, newStageImg, cancelImg, editImg;
    TextView titleTextView, stageTextView, addressTextView, responsiblesTextView, typeTextView;
    String titleStr, stageStr, addressStr, responsiblesStr, typeStr, constructionUidStr, userUidStr;
    ArrayList<String> responsiblesEmailList;

    ConstructionService service;

    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseUser user;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction_prep_view);
        context = this;
        responsiblesEmailList = new ArrayList<>();
        service = new ConstructionService(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        userUidStr = user.getUid();

        titleTextView = findViewById(R.id.construction_prep_view_title_text);
        stageTextView = findViewById(R.id.construction_prep_view_stage_text);
        addressTextView = findViewById(R.id.construction_prep_view_address_text);
        typeTextView = findViewById(R.id.construction_prep_view_type_text);
        responsiblesTextView = findViewById(R.id.construction_prep_view_responsibles_text);
        backArrowImg = findViewById(R.id.construction_prep_view_back_arrow);
        editImg = findViewById(R.id.construction_prep_view_edit);
        deleteImg = findViewById(R.id.construction_prep_view_delete);
        newStageImg = findViewById(R.id.construction_prep_view_new_stage);
        cancelImg = findViewById(R.id.construction_prep_view_cancel);

        if (getIntent().getExtras() != null) {
            titleStr = getIntent().getStringExtra("title");
            stageStr = getIntent().getStringExtra("stage");
            addressStr = getIntent().getStringExtra("address");
            typeStr = getIntent().getStringExtra("type");
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            responsiblesEmailList = getIntent().getStringArrayListExtra("responsibles");
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

        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConstructionPrepFormActivity.class);
                intent.putExtra("title", titleStr);
                intent.putExtra("address", addressStr);
                intent.putExtra("stage", stageStr);
                intent.putExtra("type", typeStr);
                intent.putStringArrayListExtra("responsibles", responsiblesEmailList);
                intent.putExtra("constructionUid", constructionUidStr);
                context.startActivity(intent);
            }
        });

        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConstructionPrepViewActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ConstructionPrepViewActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ConstructionPrepViewActivity.this);
                builder.setTitle("Крок вперед");
                builder.setMessage("Ви впевнені, що хочете змінити статус проєкту на «У процесі»?");
                AlertDialog dialog = builder.create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        service.advanceStageToExec(constructionUidStr);
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


}