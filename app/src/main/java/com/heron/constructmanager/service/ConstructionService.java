package com.heron.constructmanager.service;

import android.content.Context;
import android.net.wifi.aware.PublishConfig;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.Utils;
import com.heron.constructmanager.models.Construction;
import com.heron.constructmanager.models.Information;
import com.heron.constructmanager.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstructionService {

    Context context;
    DatabaseReference rootReference;
    FirebaseDatabase db;
    FirebaseAuth auth;
    List constructionsList;

    public ConstructionService(Context c) {
        context = c;
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://constructmanager-8c46b-default-rtdb.europe-west1.firebasedatabase.app");
        rootReference = db.getReference();
        constructionsList = new ArrayList<>();
    }

    public void writeConstructionInfo(String userId, String title, String address, String stage, String type, List<User> responsibles, String constructionUid) {
        if (constructionUid == null) {
            constructionUid = rootReference.child("constructions").push().getKey();
        }
        Information information = new Information(title, address, type, stage, responsibles);
        Construction construction = new Construction(information);
        Map<String, Object> postValues = construction.getInformation().toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/constructions/" + constructionUid + "/information", postValues);

        rootReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.WRITE);
        });
    }

    public void deleteConstruction(String constructionUid) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/constructions/" + constructionUid, null);

        rootReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.DELETE);
        });

    }

    public void cancelConstruction(String constructionUid) {
        String stage = Constants.CANCELLED;

        DatabaseReference informationReference = rootReference.child("constructions").child(constructionUid).child("information");
        informationReference.child("stage").setValue(stage).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.CANCEL);
        });
    }

    public void advanceStageToExec(String constructionUid) {
        String stage = Constants.EXEC;

        DatabaseReference informationReference = rootReference.child("constructions").child(constructionUid).child("information");
        informationReference.child("stage").setValue(stage).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.NEW_STAGE);
        });
    }

    public void advanceStageToFinished(String constructionUid) {
        String stage = Constants.FINISHED;

        DatabaseReference informationReference = rootReference.child("constructions").child(constructionUid).child("information");
        informationReference.child("stage").setValue(stage).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.NEW_STAGE);
        });
    }

    public DatabaseReference getConstructionsReference() {
        return db.getReference().child("constructions");
    }

    public boolean displayConstructionToUser(String userUid, Construction construction) {
        for (int i = 0; i < construction.getInformation().getResponsibles().size(); i++) {
            if (construction.getInformation().getResponsibles().get(i).getUid().equals(userUid)) {
                return true;
            }
        }
        return false;
    }

}