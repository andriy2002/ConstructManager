package com.heron.constructmanager.service;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.heron.constructmanager.Constants;
import com.heron.constructmanager.Utils;
import com.heron.constructmanager.models.Photo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PhotoService {
    Context context;
    DatabaseReference rootReference;
    FirebaseStorage storage;
    StorageReference bucketRef;
    FirebaseDatabase db;
    FirebaseAuth auth;

    public PhotoService(Context c) {
        context = c;
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://constructmanager-8c46b-default-rtdb.europe-west1.firebasedatabase.app");
        rootReference = db.getReference();
        storage = FirebaseStorage.getInstance();
        bucketRef = storage.getReference();
    }

    public void writePhoto(String constructionUid, String title, String desc, String photoUid) {
        DatabaseReference photosReference = rootReference.child("constructions").child(constructionUid).child("photos");
        if (photoUid == null) {
            photoUid = photosReference.push().getKey();
        }
        Photo photo = new Photo(constructionUid, title, desc);
        Map<String, Object> postValues = photo.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(photoUid, postValues);

        photosReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.WRITE);
        });
    }

    public DatabaseReference getPhotosReference(String constructionUid) {
        return db.getReference().child("constructions").child(constructionUid).child("photos");
    }

    public void uploadPhoto(String constructionUidStr, String titleStr, Uri uriFile) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();
//        File file = new File(constructionUidStr + "/" + titleStr + ".jpg");
//        Uri uriFile = Uri.fromFile(file);
        StorageReference currentImgRef = bucketRef.child("photos").child(constructionUidStr).child(titleStr);
//                bucketRef.child("images/" + uriFile.getLastPathSegment());
        UploadTask uploadTask = currentImgRef.putFile(uriFile, metadata);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "File upload NOT ok", Toast.LENGTH_LONG);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "File upload ok", Toast.LENGTH_LONG);
            }
        });
        // [END upload_file]
    }
}