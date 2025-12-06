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
import com.heron.constructmanager.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    Context context;
    DatabaseReference rootReference;
    FirebaseDatabase db;
    FirebaseAuth auth;

    public UserService(Context c) {
        context = c;
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://constructmanager-8c46b-default-rtdb.europe-west1.firebasedatabase.app");
        rootReference = db.getReference();
    }

    public void writeNewUser(String userUid, String name, String email, boolean admin, boolean isEmailVerified) {
        User user = new User(name, email, admin, isEmailVerified);
        rootReference.child("users").child(userUid).setValue(user).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.WRITE);
        });;
    }

    public DatabaseReference getUsersReference() {
        return rootReference.child("users");
    }

    public void deleteUser(String userId) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + userId, null);

        rootReference.updateChildren(childUpdates).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.DELETE);
        });
    }

    public void setUserEmailVerified(String userUid) {
        DatabaseReference userReference = rootReference.child("users").child(userUid);
        userReference.child("isEmailVerified").setValue(true).addOnCompleteListener(task -> {
            Utils.showToastMsg(context, task, Constants.UPDATE);
        });
    }

    public List<User> getUsersByEmails(List<String> selectedEmails, List<User> allUsersList) {
        List <User> allUsersMatchedEmail = new ArrayList();
        if (allUsersList.size() > 0 && selectedEmails.size() > 0) {
            for (int i = 0; i < allUsersList.size(); i++) {
                if (selectedEmails.contains(allUsersList.get(i).getEmail())) {
                    allUsersMatchedEmail.add(allUsersList.get(i));
                }
            }
        }
        return allUsersMatchedEmail;
    }

}

