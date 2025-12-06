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
import com.heron.constructmanager.activities.forms.BudgetFormActivity;
import com.heron.constructmanager.activities.forms.ReponsabilityFormActivity;
import com.heron.constructmanager.adapters.BudgetListAdapter;
import com.heron.constructmanager.adapters.BudgetListAdapter;
import com.heron.constructmanager.models.Budget;
import com.heron.constructmanager.models.Budget;
import com.heron.constructmanager.service.BudgetService;
import com.heron.constructmanager.service.BudgetService;

import java.util.ArrayList;

public class ListBudgetsActivity extends AppCompatActivity {

    Context context;

    ArrayList<Budget> budgets;
    BudgetListAdapter adapter;

    RecyclerView recyclerView;
    Button addBudgetButton;
    ImageView backArrowButton;

    FirebaseUser user;
    FirebaseAuth auth;
    BudgetService budgetService;

    String constructionUidStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_budgets);
        backArrowButton = findViewById(R.id.list_budget_back_arrow);
        addBudgetButton = findViewById(R.id.list_budget_add_button);
        recyclerView = findViewById(R.id.list_budget_recycler_view);
        budgets = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        budgetService = new BudgetService(this);
        context = this;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
        }

        adaptBudgetsToView(constructionUidStr);

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListBudgetsActivity.this, BudgetFormActivity.class);
                intent.putExtra("constructionUid", constructionUidStr);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void adaptBudgetsToView(String constructionUidStr) {
        DatabaseReference budgetsReference = budgetService.getBudgetsReference(constructionUidStr);

        budgetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                budgets = new ArrayList<>();
                Budget budget;
                for(DataSnapshot resp_snap : snapshot.getChildren()) {
                    budget = resp_snap.getValue(Budget.class);
                    budget.setBudgetUid(resp_snap.getKey());
                    budgets.add(budget);
                }
                adapter = new BudgetListAdapter(budgets, context, constructionUidStr);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListBudgetsActivity.this, "Неочікувана помилка.", Toast.LENGTH_LONG).show();
            }
        });
    }
}