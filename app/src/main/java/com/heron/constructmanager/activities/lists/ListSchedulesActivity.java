package com.heron.constructmanager.activities.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.forms.ScheduleFormActivity;
import com.heron.constructmanager.adapters.ScheduleListAdapter;
import com.heron.constructmanager.models.Schedule;
import com.heron.constructmanager.service.ScheduleService;

import java.util.ArrayList;

public class ListSchedulesActivity extends AppCompatActivity {
    Context context;

    ArrayList<Schedule> schedules;
    ScheduleListAdapter adapter;

    RecyclerView recyclerView;
    Button addScheduleButton;
    ImageView backArrowButton;

    FirebaseUser user;
    FirebaseAuth auth;
    ScheduleService scheduleService;

    String userUidStr, constructionUidStr, titleStr, stageStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedules);
        schedules = new ArrayList<>();
        context = this;

        if(getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userUidStr = auth.getCurrentUser().getUid();

        scheduleService = new ScheduleService(this);
        backArrowButton = findViewById(R.id.list_schedule_back_arrow);
        addScheduleButton = findViewById(R.id.list_schedule_add_button);

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListSchedulesActivity.this, ScheduleFormActivity.class);
                intent.putExtra("constructionUid", constructionUidStr);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.list_schedule_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adaptScheduleToView(constructionUidStr);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void adaptScheduleToView(String constructionUidStr) {
        DatabaseReference schedulesReference = scheduleService.getSchedulesReference(constructionUidStr);

        schedulesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                schedules = new ArrayList<>();
                Schedule schedule;
                for(DataSnapshot resp_snap : snapshot.getChildren()) {
                    schedule = resp_snap.getValue(Schedule.class);
                    schedule.setScheduleUid(resp_snap.getKey());
                    schedules.add(schedule);
                }
                adapter = new ScheduleListAdapter(schedules, context, constructionUidStr);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListSchedulesActivity.this, "Неочікувана помилка.", Toast.LENGTH_LONG).show();
            }
        });
    }
}