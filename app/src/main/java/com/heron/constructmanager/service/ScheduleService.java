package com.heron.constructmanager.service;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.Utils;
import com.heron.constructmanager.models.Construction;
import com.heron.constructmanager.models.Delay;
import com.heron.constructmanager.models.Information;
import com.heron.constructmanager.models.Responsability;
import com.heron.constructmanager.models.Schedule;
import com.heron.constructmanager.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleService {

    Context context;
    DatabaseReference rootReference;
    FirebaseDatabase db;
    FirebaseAuth auth;

    public ScheduleService(Context c) {
        context = c;
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://constructmanager-8c46b-default-rtdb.europe-west1.firebasedatabase.app");
        rootReference = db.getReference();
    }

    public void writeSchedule(String constructionUid, String title, String deadline, String state, String scheduleUid) {
        DatabaseReference schedulesReference = rootReference.child("constructions").child(constructionUid).child("schedules");
        if (scheduleUid == null) {
            scheduleUid = schedulesReference.push().getKey();
        }

        Schedule schedule = new Schedule(constructionUid, title, deadline, state);
        Map<String, Object> postValues = schedule.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(scheduleUid, postValues);

        schedulesReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.WRITE);
        });
    }

    public void solveSchedule(String constructionUid, String scheduleUid) {
        DatabaseReference scheduleReference = rootReference.child("constructions").child(constructionUid).child("schedules").child(scheduleUid);

        scheduleReference.child("finishDate").setValue(getTodayStringDate());
        scheduleReference.child("state").setValue(Constants.SOLVED).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.SOLVED);
        });
    }

    public void writeDelay(String constructionUid, String scheduleUid, String title, String reason, boolean isExcusable, boolean isCompensable, boolean isConcurrent, boolean isCritical, int days, String aditionalInfo, String delayUid) {
        DatabaseReference delaysReference = rootReference.child("constructions").child(constructionUid).child("schedules").child(scheduleUid).child("delays");
        if (delayUid == null) {
            delayUid = delaysReference.push().getKey();
        }

        Delay delay = new Delay(constructionUid, scheduleUid, title, reason, isExcusable, isCompensable, isConcurrent, isCritical, days, aditionalInfo);
        Map<String, Object> postValues = delay.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(delayUid, postValues);

        delaysReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.WRITE);
        });
    }

    public DatabaseReference getSchedulesReference(String constructionUid) {
        return db.getReference().child("constructions").child(constructionUid).child("schedules");
    }

    public DatabaseReference getDelaysReference(String constructionUid, String scheduleUid) {
        return db.getReference().child("constructions").child(constructionUid).child("schedules").child(scheduleUid).child("delays");
    }

    public String getTodayStringDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

}