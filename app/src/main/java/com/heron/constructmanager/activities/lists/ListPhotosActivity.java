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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.R;
import com.heron.constructmanager.activities.forms.PhotoFormActivity;
import com.heron.constructmanager.adapters.PhotoListAdapter;
import com.heron.constructmanager.models.Photo;
import com.heron.constructmanager.service.PhotoService;

import java.util.ArrayList;

public class ListPhotosActivity extends AppCompatActivity {

    Context context;

    ArrayList<Photo> photos;
    PhotoListAdapter adapter;

    RecyclerView recyclerView;
    Button addPhotoButton;
    ImageView backArrowButton;

    PhotoService photoService;

    String constructionUidStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photos);
        backArrowButton = findViewById(R.id.list_photos_back_arrow);
        addPhotoButton = findViewById(R.id.list_photos_add_button);
        recyclerView = findViewById(R.id.list_photos_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        photoService = new PhotoService(this);
        photos = new ArrayList<>();
        context = this;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
        }

        adaptPhotosToView(constructionUidStr);

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListPhotosActivity.this, PhotoFormActivity.class);
                intent.putExtra("constructionUid", constructionUidStr);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void adaptPhotosToView(String constructionUidStr) {
        DatabaseReference photosReference = photoService.getPhotosReference(constructionUidStr);

        photosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photos = new ArrayList<>();
                Photo photo;
                for(DataSnapshot resp_snap : snapshot.getChildren()) {
                    photo = resp_snap.getValue(Photo.class);
                    photo.setPhotoUid(resp_snap.getKey());
                    photos.add(photo);
                }
                adapter = new PhotoListAdapter(context, photos, constructionUidStr);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListPhotosActivity.this, Constants.UNEXPECTED_ERROR, Toast.LENGTH_LONG).show();
            }
        });
    }
}