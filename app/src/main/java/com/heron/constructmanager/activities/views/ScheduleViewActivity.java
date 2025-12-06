package com.heron.constructmanager.activities.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.forms.DelayFormActivity;
import com.heron.constructmanager.activities.lists.ListResponsabilitiesActivity;
import com.heron.constructmanager.activities.lists.ListSchedulesActivity;
import com.heron.constructmanager.adapters.DelayListAdapter;
import com.heron.constructmanager.adapters.ScheduleListAdapter;
import com.heron.constructmanager.models.Delay;
import com.heron.constructmanager.models.Schedule;
import com.heron.constructmanager.service.ScheduleService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleViewActivity extends AppCompatActivity {

    Context context;
    ImageView backArrowImg, addDelayImg, emailImg, responsabilitiesImg, solveImg;
    TextView titleTextView, stateTextView, deadlineTextView, delayLabelTextView, finishDateTextView;
    CardView stateCard, finishDateCard;
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;
    ScheduleService scheduleService;
    ArrayList<Delay> delays;
    DelayListAdapter adapter;

    String constructionUidStr, scheduleUidStr, titleStr, stateStr, deadlineStr, finishDateStr;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        context = this;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        linearLayoutManager = new LinearLayoutManager(this);
        scheduleService = new ScheduleService(this);

        titleTextView = findViewById(R.id.schedule_view_title_text);
        stateTextView = findViewById(R.id.schedule_view_state_text);
        deadlineTextView = findViewById(R.id.schedule_view_deadline_text);
        stateCard = findViewById(R.id.schedule_view_state_card);
        finishDateCard = findViewById(R.id.schedule_view_finish_date_card);
        delayLabelTextView = findViewById(R.id.schedule_view_delay_label);
        finishDateTextView = findViewById(R.id.schedule_view_finish_date_text);
        backArrowImg = findViewById(R.id.schedule_view_back_arrow);
        addDelayImg = findViewById(R.id.schedule_view_add_delay_button);
        emailImg = findViewById(R.id.schedule_view_email_button);
        solveImg = findViewById(R.id.schedule_view_solve_img);
        responsabilitiesImg = findViewById(R.id.schedule_view_responsabilities_img);
        recyclerView = findViewById(R.id.schedule_view_delays_recycler_view);

        if (getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            scheduleUidStr = getIntent().getStringExtra("scheduleUid");
            titleStr = getIntent().getStringExtra("title");
            stateStr = getIntent().getStringExtra("state");
            deadlineStr = getIntent().getStringExtra("deadline");
            finishDateStr = getIntent().getStringExtra("finishDate");
        }

        titleTextView.setText(titleStr);
        stateTextView.setText(stateStr);
        deadlineTextView.setText(deadlineStr);
        finishDateTextView.setText(finishDateStr);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adaptDelaysToView(constructionUidStr, scheduleUidStr);

        if (stateStr != null && stateStr.equals(Constants.SOLVED)) {
            delayLabelTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            finishDateCard.setVisibility(View.VISIBLE);
            solveImg.setVisibility(View.INVISIBLE);
            stateCard.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        }
        else if (delayExists(deadlineStr, null)) {
            showInfoDelayDialog();
            recyclerView.setVisibility(View.VISIBLE);
            addDelayImg.setVisibility(View.VISIBLE);
            emailImg.setVisibility(View.VISIBLE);
            delayLabelTextView.setVisibility(View.VISIBLE);
            stateCard.setBackgroundColor(ContextCompat.getColor(this, R.color.lightred));
            stateTextView.setText(Constants.LATE);
        }

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addDelayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleViewActivity.this, DelayFormActivity.class);
                intent.putExtra("constructionUid", constructionUidStr);
                intent.putExtra("scheduleUid", scheduleUidStr);
                startActivity(intent);
            }
        });

        responsabilitiesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleViewActivity.this, ListResponsabilitiesActivity.class);
                intent.putExtra("constructionUid", constructionUidStr);
                startActivity(intent);
            }
        });

        solveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleViewActivity.this);
                builder.setTitle("Завершити розклад");
                builder.setMessage("Чи всі кроки у часовій шкалі виконано?");
                AlertDialog dialog = builder.create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scheduleService.solveSchedule(constructionUidStr, scheduleUidStr);
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

    @SuppressLint("SimpleDateFormat")
    public boolean delayExists(String deadline, SimpleDateFormat dateFormatter) {
        Date dateDeadline;
        Date dateToday;
        if (dateFormatter == null) {
            dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        }
        try {
            dateDeadline = dateFormatter.parse(deadline);
            dateToday = new Date();

            if(dateToday.compareTo(dateDeadline) > 0) {
                return true;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void showInfoDelayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleViewActivity.this);
        builder.setTitle("Увага");
        builder.setMessage(
                "Поточний графік має затримку.\n" +
                        "Натисніть на іконку E-mail, щоб повідомити відповідальних;\n\n" +
                        "Натисніть на іконку Відповідальності, щоб призначити виконавців для усунення затримки;\n\n" +
                        "Натисніть на іконку Додати, щоб зареєструвати та класифікувати причини затримки.");

        AlertDialog dialog = builder.create();
        dialog.setButton(Dialog.BUTTON_POSITIVE, "Зрозумів", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void adaptDelaysToView(String constructionUid, String scheduleUid) {
        DatabaseReference delaysReference = scheduleService.getDelaysReference(constructionUid, scheduleUid);

        delaysReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                delays = new ArrayList<>();
                Delay delay;
                for(DataSnapshot delaySnap : snapshot.getChildren()) {
                    delay = delaySnap.getValue(Delay.class);
                    delay.setDelayUid(delaySnap.getKey());
                    delays.add(delay);
                }
                adapter = new DelayListAdapter(delays, context, constructionUid, scheduleUid);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ScheduleViewActivity.this, "Неочікувана помилка.", Toast.LENGTH_LONG).show();
            }
        });
    }



}