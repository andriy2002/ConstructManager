package com.heron.constructmanager.service;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.Utils;
import com.heron.constructmanager.models.Responsability;
import java.util.HashMap;
import java.util.Map;

public class ResponsabilityService {

    Context context;
    DatabaseReference rootReference;
    FirebaseDatabase db;
    FirebaseAuth auth;

    public ResponsabilityService(Context c) {
        context = c;
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://constructmanager-8c46b-default-rtdb.europe-west1.firebasedatabase.app");
        rootReference = db.getReference();
    }

    public void writeResponsability(String constructionUid, String responsabilityUid, String title, String desc, String deadline, String state, String responsibleEmail) {
        DatabaseReference constructionReference = rootReference.child("constructions").child(constructionUid);
        if (responsabilityUid == null) {
            responsabilityUid = constructionReference.child("responsabilities").push().getKey();
        }

        Responsability responsability = new Responsability(constructionUid, title, desc, deadline, state, responsibleEmail);
        Map<String, Object> postValues = responsability.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/responsabilities/" + responsabilityUid, postValues);

        constructionReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.WRITE);
        });
    }

    public void solveResponsability(String constructionUid, String responsabilityUid) {
        DatabaseReference responsabilityReference = rootReference.child("constructions").child(constructionUid).child("responsabilities").child(responsabilityUid);
        responsabilityReference.child("state").setValue(Constants.SOLVED).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.SOLVED);
        });
    }

    public DatabaseReference getResponsabilitiesReference(String constructionUid) {
        return db.getReference().child("constructions").child(constructionUid).child("responsabilities");
    }

}