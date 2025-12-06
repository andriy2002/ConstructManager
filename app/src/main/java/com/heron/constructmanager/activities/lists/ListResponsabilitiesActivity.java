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
import com.heron.constructmanager.activities.forms.ReponsabilityFormActivity;
import com.heron.constructmanager.adapters.ResponsabilityListAdapter;
import com.heron.constructmanager.models.Responsability;
import com.heron.constructmanager.service.ResponsabilityService;

import java.util.ArrayList;

public class ListResponsabilitiesActivity extends AppCompatActivity {

    Context context;

    ArrayList<Responsability> responsabilities;
    ArrayList<String> responsiblesEmailList;
    ResponsabilityListAdapter adapter;

    RecyclerView recyclerView;
    Button addResponsabilityButton;
    ImageView backArrowButton;

    FirebaseUser user;
    FirebaseAuth auth;
    ResponsabilityService responsabilityService;

    String constructionUidStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_responsabilities);
        backArrowButton = findViewById(R.id.list_responsabilities_back_arrow);
        addResponsabilityButton = findViewById(R.id.list_responsabilities_add_button);
        responsabilities = new ArrayList<>();
        responsiblesEmailList = new ArrayList<>();
        responsabilityService = new ResponsabilityService(this);
        context = this;

        if(getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addResponsabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListResponsabilitiesActivity.this, ReponsabilityFormActivity.class);
                intent.putExtra("constructionUid", constructionUidStr);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.list_responsabilities_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adaptResponsabilitiesToView(constructionUidStr);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void adaptResponsabilitiesToView(String constructionUidStr) {
        DatabaseReference responsabilitiesReference = responsabilityService.getResponsabilitiesReference(constructionUidStr);

        responsabilitiesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                responsabilities = new ArrayList<>();
                Responsability responsability;
                for(DataSnapshot resp_snap : snapshot.getChildren()) {
                    responsability = resp_snap.getValue(Responsability.class);
                    responsability.setResponsabilityUid(resp_snap.getKey());
                    responsabilities.add(responsability);
                }
                adapter = new ResponsabilityListAdapter(responsabilities, context, constructionUidStr);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListResponsabilitiesActivity.this, "Неочікувана помилка.", Toast.LENGTH_LONG).show();
            }
        });
    }

}