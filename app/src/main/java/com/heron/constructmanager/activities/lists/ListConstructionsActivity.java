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
import com.heron.constructmanager.adapters.ConstructionListAdapter;
import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.forms.ConstructionPrepFormActivity;
import com.heron.constructmanager.models.Construction;
import com.heron.constructmanager.models.Responsability;
import com.heron.constructmanager.models.User;
import com.heron.constructmanager.service.ConstructionService;

import java.util.ArrayList;

public class ListConstructionsActivity extends AppCompatActivity {

    Context context;

    ArrayList<Construction> constructions;
    ArrayList<User> responsibles;

    ConstructionListAdapter adapter;

    RecyclerView recyclerView;

    Button addConstructionButton;
    ImageView backArrowButton;

    FirebaseUser user;
    FirebaseAuth auth;
    ConstructionService service;

    String userUidStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_constructions);
        constructions = new ArrayList<>();
        context = this;

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userUidStr = auth.getCurrentUser().getUid();
        service = new ConstructionService(this);

        backArrowButton = findViewById(R.id.list_constructions_back_arrow);
        addConstructionButton = findViewById(R.id.list_constructions_add_button);

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addConstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListConstructionsActivity.this, ConstructionPrepFormActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.list_constructions_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adaptConstructionsToView(userUidStr);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // ADAPT CONSTRUCTIONS SHOWN BASED ON USER LOGGED IN

    private void adaptConstructionsToView(String userUid) {
        DatabaseReference constructionsReference = service.getConstructionsReference();

        constructionsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                constructions = new ArrayList<>();
                Construction construction;
                for(DataSnapshot construction_snap : snapshot.getChildren()) {
                    construction = construction_snap.getValue(Construction.class);
                    construction.setUid(construction_snap.getKey());

                    responsibles = new ArrayList<>();
                    User responsible;
                    for(DataSnapshot responsible_snap : construction_snap.child("information").child("responsibles").getChildren()) {
                        responsible = responsible_snap.getValue(User.class);
                        responsible.setUid(responsible_snap.child("uid").getValue(String.class));
                        responsibles.add(responsible);
                    }
                    construction.getInformation().setResponsibles(responsibles);
                    if (service.displayConstructionToUser(userUid, construction)){
                        constructions.add(construction);
                    }
                }
                adapter = new ConstructionListAdapter(constructions, context);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListConstructionsActivity.this, "Неочікувана помилка.", Toast.LENGTH_LONG).show();
            }
        });
    }
}