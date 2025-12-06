package com.heron.constructmanager.activities.forms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.service.PhotoService;

import java.io.File;

public class PhotoFormActivity extends AppCompatActivity {

    ImageView backArrowImg, uploadImg;
    EditText titleEditText, descEditText;
    Button addButton;

    PhotoService photoService;
    ValidateInput validateInput;
    StorageReference photosRef;

    String constructionUidStr, titleStr, descStr, photoUidStr;

    private static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_form);
        backArrowImg = findViewById(R.id.photo_form_back_arrow);
        titleEditText = findViewById(R.id.photo_form_title);
        descEditText = findViewById(R.id.photo_form_desc);
        addButton = findViewById(R.id.photo_form_add_button);
        uploadImg = findViewById(R.id.photo_form_upload_button);
        photoService = new PhotoService(this);
        photosRef = FirebaseStorage.getInstance().getReference().child("photos");

        if (getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            photoUidStr = getIntent().getStringExtra("photoUid");
            descStr = getIntent().getStringExtra("desc");
            titleStr = getIntent().getStringExtra("title");
        }
        titleEditText.setText(titleStr);
        descEditText.setText(descStr);

        validateInput = new ValidateInput(PhotoFormActivity.this, titleEditText, descEditText);

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infosVerified()) {
                    getEditTextsContent();
                    photoService.writePhoto(constructionUidStr, titleStr, descStr, photoUidStr);
                    finish();
                }
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infosVerified()) {
                    getEditTextsContent();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            Uri uri = data.getData();
            photosRef.child(constructionUidStr).child(titleStr + ".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PhotoFormActivity.this, "File upload ok", Toast.LENGTH_LONG);
                }
            });
        }
    }

    public boolean infosVerified() {
        boolean titleVerified = validateInput.validateTitle();
        boolean descVerified = validateInput.validateDesc();
        return titleVerified && descVerified;
    }

    public void getEditTextsContent() {
        titleStr = titleEditText.getText().toString().trim();
        descStr = descEditText.getText().toString().trim();
    }
}