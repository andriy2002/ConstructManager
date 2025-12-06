package com.heron.constructmanager.activities.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.heron.constructmanager.animations.LoadingAnimation;
import com.heron.constructmanager.R;
import com.heron.constructmanager.ValidateInput;
import com.heron.constructmanager.models.User;
import com.heron.constructmanager.service.ConstructionService;
import com.heron.constructmanager.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ConstructionPrepFormActivity extends AppCompatActivity {

    MultiAutoCompleteTextView nachoTextView;
    ImageView backArrowImg;
    EditText titleEditText, addressEditText, typeEditText;
    Button addButton;

    List<User> selectedUsersList = new ArrayList<>();
    List<User> allUsersList = new ArrayList<>();
    List<String> selectedEmailsList = new ArrayList<>();
    List<String> allEmailsList = new ArrayList<>();

    FirebaseAuth auth;
    FirebaseUser user;
    ConstructionService service;
    UserService userService;

    String userIdStr, titleStr, addressStr, stageStr, typeStr, constructionUidStr;

    ValidateInput validateInput;
    LoadingAnimation loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction_prep_form);

        constructionUidStr = null;
        stageStr = "Підготовка";
        service = new ConstructionService(this);
        userService = new UserService(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userIdStr = user.getUid();

        // Components
        titleEditText = findViewById(R.id.construction_prep_title);
        addressEditText = findViewById(R.id.construction_prep_address);
        typeEditText = findViewById(R.id.construction_prep_type);
        addButton = findViewById(R.id.construction_prep_add_button);
        backArrowImg = findViewById(R.id.construction_prep_back_arrow);
        nachoTextView = findViewById(R.id.construction_prep_nacho_res_text_view);

        if(getIntent().getExtras() != null) {
            constructionUidStr = getIntent().getStringExtra("constructionUid");
            titleStr = getIntent().getStringExtra("title");
            addressStr = getIntent().getStringExtra("address");
            typeStr = getIntent().getStringExtra("type");
        }

        titleEditText.setText(titleStr);
        addressEditText.setText(addressStr);
        typeEditText.setText(typeStr);

        // Validate
        validateInput = new ValidateInput(ConstructionPrepFormActivity.this, titleEditText, addressEditText, typeEditText, null);

        // Loading animation
        loading = new LoadingAnimation(this);

        // Populate users
        DatabaseReference usersReference = userService.getUsersReference();
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allEmailsList.clear();
                allUsersList.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    User user = childSnapshot.getValue(User.class);
                    user.setUid(childSnapshot.getKey());
                    String email = childSnapshot.child("email").getValue(String.class);
                    allUsersList.add(user);
                    allEmailsList.add(email);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ConstructionPrepFormActivity.this, android.R.layout.simple_dropdown_item_1line, allEmailsList);
                nachoTextView.setAdapter(arrayAdapter);
                nachoTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // Listeners
        backArrowImg.setOnClickListener(v -> finish());

        addButton.setOnClickListener(v -> {
            loading.loadingAnimationDialog();
            if (infosVerified()) {
                getEditTextsContent();
                selectedUsersList = userService.getUsersByEmails(selectedEmailsList, allUsersList);
                service.writeConstructionInfo(userIdStr, titleStr, addressStr, stageStr, typeStr, selectedUsersList, constructionUidStr);
                loading.dismissLoading();
                finish();
            } else {
                loading.dismissLoading();
            }
        });
    }

    public boolean infosVerified() {
        boolean title_verified = validateInput.validateTitle();
        boolean address_verified = validateInput.validateAddress();
        boolean type_verified = validateInput.validateType();
        boolean nachoVerified = !nachoTextView.getText().toString().trim().isEmpty();
        return title_verified && address_verified && type_verified && nachoVerified;
    }

    public void getEditTextsContent() {
        titleStr = titleEditText.getText().toString().trim();
        addressStr = addressEditText.getText().toString().trim();
        typeStr = typeEditText.getText().toString().trim();

        selectedEmailsList = new ArrayList<>();
        String text = nachoTextView.getText().toString().trim();
        for (String email : text.split(",")) {
            if (!email.trim().isEmpty())
                selectedEmailsList.add(email.trim());
        }
    }
}
