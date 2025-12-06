package com.heron.constructmanager.service;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.Utils;
import com.heron.constructmanager.models.Budget;

import java.util.HashMap;
import java.util.Map;

public class BudgetService {

    Context context;
    DatabaseReference rootReference;
    FirebaseDatabase db;
    FirebaseAuth auth;

    public BudgetService(Context c) {
        context = c;
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://constructmanager-8c46b-default-rtdb.europe-west1.firebasedatabase.app");
        rootReference = db.getReference();
    }

    public void writeBudget(String constructionUid, String title, String desc, float value, String budgetUid) {
        DatabaseReference constructionReference = rootReference.child("constructions").child(constructionUid);
        if (budgetUid == null) {
            budgetUid = constructionReference.child("budgets").push().getKey();
        }
        Budget budget = new Budget(constructionUid, title, desc, value);
        Map<String, Object> postValues = budget.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/budgets/" + budgetUid, postValues);

        constructionReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.WRITE);
        });
    }

    public DatabaseReference getBudgetsReference(String constructionUid) {
        return db.getReference().child("constructions").child(constructionUid).child("budgets");
    }

}